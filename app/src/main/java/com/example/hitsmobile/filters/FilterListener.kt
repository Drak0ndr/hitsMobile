package com.example.hitsmobile.filters

import com.example.hitsmobile.filters.PhotoFilter
interface FilterListener {
    fun onFilterSelected(photoFilter: PhotoFilter)
}