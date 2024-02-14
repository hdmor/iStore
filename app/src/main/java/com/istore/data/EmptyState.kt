package com.istore.data

import androidx.annotation.StringRes

/**
 * for show & hide empty-state layout by viewModel in cartFragment
 */
data class EmptyState(
    val visibility: Boolean,
    @StringRes val messageResourceId: Int = 0,
    val callToActionButtonVisibility: Boolean = false
)
