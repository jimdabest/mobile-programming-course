package com.uth.week03.uicomponents

sealed class Screen(val route: String) {
    object Ready : Screen("ready_screen")
    object ComponentList : Screen("component_list_screen")
    object TextDetail : Screen("text_detail_screen")
    object ImageDetail : Screen("image_detail_screen")
    object TextFieldDetail : Screen("textfield_detail_screen")
    object RowLayoutDetail : Screen("row_layout_screen")
    object SelfResearchDetail : Screen("self_research_screen")
}