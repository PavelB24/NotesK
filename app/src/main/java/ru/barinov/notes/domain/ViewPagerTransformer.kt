package ru.barinov.notes.domain

import android.view.View
import androidx.annotation.NonNull

import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

private const val MIN_SCALE = 0.65f
private const val MIN_ALPHA = 0.3f


class ViewPagerTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (position < -1) {  // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0F);
        } else if (position <= 1) { // [-1,1]
            page.scaleX = MIN_SCALE.coerceAtLeast(1 - abs(position));
            page.scaleY = MIN_SCALE.coerceAtLeast(1 - abs(position));
            page.alpha = MIN_ALPHA.coerceAtLeast(1 - abs(position));
        } else {  // (1,+Infinity]
            // This page is way off-screen to the right.
            page.alpha = 0F;
        }
}}

