package com.example.android

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class GraphItem(val name: String, val children: ArrayList<GraphItem>?) {
    var parent: GraphItem? = null
}



class GraphView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    interface GraphViewActionListener {
        fun onTopicClicked(item: GraphItem)
        fun onFocusingTopicChanged(before: GraphItem?, after: GraphItem?)
    }


    //// private

    // 데이터
    var itemList: ArrayList<GraphItem> = ArrayList()
        set(value) {
            field = value
            currentItem = null
            updateParent()
            updateLayout()
        }
    private var currentItem: GraphItem? = null // null일 경우 itemList를 그대로 사용, 아닐 경우 해당 item의 children을 사용

    // 터치 관련
    private var gestureDetector: GestureDetector
    var actionListener: GraphViewActionListener? = null

    // 그리기 관련
    private val colorPalette = arrayOf(
        Color.rgb(219, 17, 180),
        Color.rgb(10, 41, 174),
        Color.rgb(235, 153, 0),
        Color.rgb(255, 51, 0),
        Color.rgb(170, 170, 170),
        Color.rgb(255, 79, 176),
        Color.rgb(64, 0, 0),
        Color.rgb(255, 79, 16)
    )
    private val marginOut = context.resources.displayMetrics.density * 5
    private val marginIn = context.resources.displayMetrics.density * 10
    private val maxItemSize = context.resources.displayMetrics.density * 100
    private val noticeTextSize = context.resources.displayMetrics.density * 20
    private val baseRect = RectF()
    private val itemRectList: ArrayList<RectF> = ArrayList()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textSize = 0f


    init {
        setBackgroundColor(Color.WHITE)

        // GestureDetector
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                // 뭔가 눌렀다
                val targetList = (if(currentItem == null) itemList else currentItem?.children) ?: ArrayList()
                for(i in 0 until itemRectList.size) {
                    // 아이템을 눌렀다
                    if(isInCircle(e.x, e.y, itemRectList[i])) {
                        actionListener?.onTopicClicked(targetList[i])
                        actionListener?.onFocusingTopicChanged(currentItem, targetList[i])
                        currentItem = targetList[i]
                        updateLayout()
                        return true
                    }
                }
                // 바깥 영역을 눌렀다
                actionListener?.onFocusingTopicChanged(currentItem, currentItem?.parent)
                currentItem = currentItem?.parent
                updateLayout()
                return true
            }

        })

    }


    //// data process

    private fun updateParent() { updateParent(null) }
    private fun updateParent(target: GraphItem?) {

        // null일 경우 itemList 그대로 사용
        val list = (if(target == null) itemList else target.children) ?: return
        for(item in list) {
            item.parent = target
            updateParent(item)
        }

    }


    //// Layout

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed) {
            updateLayout()
        }
    }


    private fun updateLayout() {

        // 화면에 표시할 리스트
        val targetList = (if(currentItem == null) itemList else currentItem?.children)

        // 그리기 영역 지정
        itemRectList.clear()
        baseRect.set(0f, 0f, width.toFloat(), height.toFloat())
        baseRect.inset(marginOut, marginOut)
        baseRect.top += marginIn * 2 + noticeTextSize
        when(targetList?.size) {
            1 -> itemRectList.add(baseRect)
            2 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 1f, 0f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 1f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 1f, 0f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 1f, 0f)))
            }
            3 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 2f, 0f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 2f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 2f, 0f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 2f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 1f, 0f), topOfRect(baseRect, 2f, 1f), rightOfRect(baseRect, 1f, 0f), bottomOfRect(baseRect, 2f, 1f)))
            }
            4 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 2f, 0f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 2f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 2f, 0f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 2f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 2f, 1f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 2f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 2f, 1f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 2f, 1f)))
            }
            5 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 1f, 0f), topOfRect(baseRect, 3f, 0f), rightOfRect(baseRect, 1f, 0f), bottomOfRect(baseRect, 3f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 3f, 1f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 3f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 3f, 1f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 3f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 3f, 2f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 3f, 2f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 3f, 2f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 3f, 2f)))
            }
            6 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 3f, 0f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 3f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 3f, 0f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 3f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 3f, 1f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 3f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 3f, 1f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 3f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 3f, 2f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 3f, 2f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 3f, 2f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 3f, 2f)))
            }
            7 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 1f, 0f), topOfRect(baseRect, 4f, 0f), rightOfRect(baseRect, 1f, 0f), bottomOfRect(baseRect, 4f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 4f, 1f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 4f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 4f, 1f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 4f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, -0.25f), topOfRect(baseRect, 4f, 2f), rightOfRect(baseRect, 2f, -0.25f), bottomOfRect(baseRect, 4f, 2f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1.25f), topOfRect(baseRect, 4f, 2f), rightOfRect(baseRect, 2f, 1.25f), bottomOfRect(baseRect, 4f, 2f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 4f, 3f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 4f, 3f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 4f, 3f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 4f, 3f)))
            }
            8 -> {
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 4f, 0f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 4f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 4f, 0f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 4f, 0f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 4f, 1f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 4f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 4f, 1f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 4f, 1f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 4f, 2f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 4f, 2f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 4f, 2f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 4f, 2f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 0f), topOfRect(baseRect, 4f, 3f), rightOfRect(baseRect, 2f, 0f), bottomOfRect(baseRect, 4f, 3f)))
                itemRectList.add(RectF(leftOfRect(baseRect, 2f, 1f), topOfRect(baseRect, 4f, 3f), rightOfRect(baseRect, 2f, 1f), bottomOfRect(baseRect, 4f, 3f)))
            }
        }

        // 동일한 크기의 정사각형으로 보정
        var size = 99999f
        for (rect in itemRectList) {
            size = min(size, min(rect.width(), rect.height()))
        }
        for (rect in itemRectList) {
            val xDiff = (rect.width() - size) / 2
            val yDiff = (rect.height() - size) / 2
            rect.inset(xDiff, yDiff)
            if(rect.centerX() > baseRect.centerX()) {
                rect.left -= xDiff
                rect.right -= xDiff
            } else if(rect.centerX() < baseRect.centerX()) {
                rect.left += xDiff
                rect.right += xDiff
            }
            if(rect.centerY() > baseRect.centerY()) {
                rect.top -= yDiff
                rect.bottom -= yDiff
            } else if(rect.centerY() < baseRect.centerY()) {
                rect.top += yDiff
                rect.bottom += yDiff
            }
        }

        // 텍스트 크기는 원의 1/3로 한다
        textSize = size / 5


        invalidate()


    }


    private fun leftOfRect(rect: RectF, size: Float, position: Float) : Float {
        val unitWidth = (rect.width() - marginIn * (size - 1)) / size
        return rect.left + unitWidth * position + marginIn * position
    }
    private fun rightOfRect(rect: RectF, size: Float, position: Float) : Float {
        val unitWidth = (rect.width() - marginIn * (size - 1)) / size
        return rect.left + unitWidth * (position + 1) + marginIn * position
    }
    private fun topOfRect(rect: RectF, size: Float, position: Float) : Float {
        val unitHeight = (rect.height() - marginIn * (size - 1)) / size
        return rect.top + unitHeight * position + marginIn * position
    }
    private fun bottomOfRect(rect: RectF, size: Float, position: Float) : Float {
        val unitHeight = (rect.height() - marginIn * (size - 1)) / size
        return rect.top + unitHeight * (position + 1) + marginIn * position
    }
    private fun isInCircle(x: Float, y: Float, circle: RectF) : Boolean {
        return sqrt((circle.centerX() - x).pow(2) + (circle.centerY() - y).pow(2)) <= min(circle.width(), circle.height()) / 2
    }



    //// Touch Event

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }


    //// Drawing


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 화면에 표시할 리스트
        val targetList = (if(currentItem == null) itemList else currentItem?.children) ?: ArrayList()

        // 타이틀
        paint.textSize = noticeTextSize
        paint.color = Color.BLACK
        val title = currentItem?.name ?: "Main Topic"
        val titleWidth = paint.measureText(title)
        canvas.drawText(title, baseRect.centerX() - titleWidth / 2, marginIn - paint.ascent(), paint)

        // 빈 텍스트
        if(itemRectList.isEmpty()) {
            paint.textSize = noticeTextSize
            paint.color = Color.GRAY
            val emptyText = "No Item"
            val emptyTextWidth = paint.measureText(emptyText)
            canvas.drawText(emptyText, baseRect.centerX() - emptyTextWidth / 2, baseRect.centerY() + paint.descent(), paint)
            return
        }

        // TextPaint
        val textPaint = TextPaint()
        for (i in 0 until itemRectList.size) {
            // 원 그리기
            paint.color = colorPalette[i % colorPalette.size]
            canvas.drawOval(itemRectList[i], paint)

            // 글자 그리기
            paint.color = Color.WHITE
            paint.textSize = textSize
            textPaint.set(paint)
            val textRect = RectF(itemRectList[i])
            textRect.inset(textRect.width() / 8, textRect.height() / 8)
            val layout = StaticLayout(targetList[i].name, textPaint, textRect.width().toInt(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true)
            val textHeight = layout.height.toFloat()
            canvas.save()
            canvas.clipRect(textRect, Region.Op.INTERSECT)
            canvas.translate(textRect.left, textRect.top + (textRect.height() - textHeight) / 2)
            layout.draw(canvas)
            canvas.restore()
        }



    }



}