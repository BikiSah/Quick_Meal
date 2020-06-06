package com.biki.quickmeal

import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

fun <T> mockPagedList(list: List<T>): PagedList<T> {
    val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
    whenever(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
        val index = invocation.arguments.first() as Int
        list[index]
    }
    whenever(pagedList.size).thenReturn(list.size)
    return pagedList
}