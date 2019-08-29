package androidx.recyclerview.widget

import android.view.View
import android.view.ViewGroup

class LinearWithoutDecorationsSnapHelper : SnapHelper(){


    private val INVALID_DISTANCE = 1f
    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager, targetView: View
    ): IntArray {
        val out = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(
                layoutManager, targetView,
                getHorizontalHelper(layoutManager)
            )
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(
                layoutManager, targetView,
                getVerticalHelper(layoutManager)
            )
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager, velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return RecyclerView.NO_POSITION
        }

        val itemCount = layoutManager.itemCount
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION
        }

        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION

        val currentPosition = layoutManager.getPosition(currentView)
        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION
        }

        val vectorProvider = layoutManager as RecyclerView.SmoothScroller.ScrollVectorProvider
        // deltaJumps sign comes from the velocity which may not match the order of children in
        // the LayoutManager. To overcome this, we ask for a vector from the LayoutManager to
        // get the direction.
        val vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1)
            ?: // cannot get a vector for the given position.
            return RecyclerView.NO_POSITION

        var vDeltaJump: Int
        var hDeltaJump: Int
        if (layoutManager.canScrollHorizontally()) {
            hDeltaJump = estimateNextPositionDiffForFling(
                layoutManager,
                getHorizontalHelper(layoutManager), velocityX, 0
            )
            if (vectorForEnd.x < 0) {
                hDeltaJump = -hDeltaJump
            }
        } else {
            hDeltaJump = 0
        }
        if (layoutManager.canScrollVertically()) {
            vDeltaJump = estimateNextPositionDiffForFling(
                layoutManager,
                getVerticalHelper(layoutManager), 0, velocityY
            )
            if (vectorForEnd.y < 0) {
                vDeltaJump = -vDeltaJump
            }
        } else {
            vDeltaJump = 0
        }

        val deltaJump = if (layoutManager.canScrollVertically()) vDeltaJump else hDeltaJump
        if (deltaJump == 0) {
            return RecyclerView.NO_POSITION
        }

        var targetPos = currentPosition + deltaJump
        if (targetPos < 0) {
            targetPos = 0
        }
        if (targetPos >= itemCount) {
            targetPos = itemCount - 1
        }
        return targetPos
    }

    private fun distanceToCenter(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View, helper: OrientationHelper
    ): Int {
        val childCenter =
            helper.getDecoratedStart(targetView) + helper.getDecoratedMeasurement(targetView) / 2
        val containerCenter: Int
        if (layoutManager.clipToPadding) {
            containerCenter = helper.startAfterPadding + helper.totalSpace / 2
        } else {
            containerCenter = helper.end / 2
        }
        return childCenter - containerCenter
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager))
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager))
        }
        return null
    }

    private fun estimateNextPositionDiffForFling(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper, velocityX: Int, velocityY: Int
    ): Int {
        val distances = calculateScrollDistance(velocityX, velocityY)
        val distancePerChild = computeDistancePerChild(layoutManager, helper)
        if (distancePerChild <= 0) {
            return 0
        }
        val distance =
            if (Math.abs(distances[0]) > Math.abs(distances[1])) distances[0] else distances[1]
        return Math.round(distance / distancePerChild)
    }

    fun findCenterView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                layoutManager.getChildAt(0)!!.callOnClick()
                return layoutManager.getChildAt(0)
            } else if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                //layoutManager.getChildAt(layoutManager.itemCount - 1)!!.callOnClick()
                return layoutManager.getChildAt(layoutManager.itemCount - 1)
            }
        }

        var closestChild: View? = null
        var closestChildPosition: Int? = null
        val center: Int
        if (layoutManager.clipToPadding) {
            center = helper.startAfterPadding + helper.totalSpace / 2
        } else {
            center = helper.end / 2
        }
        var absClosest = Integer.MAX_VALUE

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val childCenter =
                helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2
            val absDistance = Math.abs(childCenter - center)

            /** if child center is closer than previous closest, set it as closest   */
            if (absDistance < absClosest) {
                closestChildPosition = i
                absClosest = absDistance
                closestChild = child
            }
        }
        if (closestChildPosition != null) layoutManager.getChildAt(closestChildPosition)!!.callOnClick()
        return closestChild
    }

    private fun computeDistancePerChild(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): Float {
        var minPosView: View? = null
        var maxPosView: View? = null
        var minPos = Integer.MAX_VALUE
        var maxPos = Integer.MIN_VALUE
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return INVALID_DISTANCE
        }

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val pos = layoutManager.getPosition(child!!)
            if (pos == RecyclerView.NO_POSITION) {
                continue
            }
            if (pos < minPos) {
                minPos = pos
                minPosView = child
            }
            if (pos > maxPos) {
                maxPos = pos
                maxPosView = child
            }
        }
        if (minPosView == null || maxPosView == null) {
            return INVALID_DISTANCE
        }
        val start = Math.min(
            helper.getDecoratedStart(minPosView),
            helper.getDecoratedStart(maxPosView)
        )
        val end = Math.max(
            helper.getDecoratedEnd(minPosView),
            helper.getDecoratedEnd(maxPosView)
        )
        val distance = end - start
        return if (distance == 0) {
            INVALID_DISTANCE
        } else 1f * distance / (maxPos - minPos + 1)
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mVerticalHelper == null || mVerticalHelper!!.mLayoutManager !== layoutManager) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper!!
    }

    private fun getHorizontalHelper(
        layoutManager: RecyclerView.LayoutManager
    ): OrientationHelper {
        if (mHorizontalHelper == null || mHorizontalHelper!!.mLayoutManager !== layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }
}