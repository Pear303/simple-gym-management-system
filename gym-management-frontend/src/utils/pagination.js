import { ref, computed } from 'vue'

export function createPagination(pageSize = 30) {
  const currentPage = ref(1)
  const currentPageSize = ref(pageSize)
  const fullData = ref([])
  const total = ref(0)
  // 是否使用服务端分页（服务端分页时 pagedData 直接返回 fullData，不做切片）
  const serverPaged = ref(false)

  const pagedData = computed(() => {
    if (serverPaged.value) {
      return fullData.value
    }
    const start = (currentPage.value - 1) * currentPageSize.value
    const end = start + currentPageSize.value
    return fullData.value.slice(start, end)
  })

  const updateData = (data, totalCount, isServerPaged = false) => {
    fullData.value = data || []
    total.value = totalCount !== undefined ? totalCount : fullData.value.length
    serverPaged.value = isServerPaged
  }

  const handlePageChange = (page) => {
    currentPage.value = page
  }

  const handleSizeChange = (size) => {
    currentPageSize.value = size
    currentPage.value = 1
  }

  return {
    currentPage,
    currentPageSize,
    pagedData,
    total,
    updateData,
    handlePageChange,
    handleSizeChange
  }
}
