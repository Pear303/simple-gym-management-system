import { ref, computed } from 'vue'

export function createPagination(pageSize = 30) {
  const currentPage = ref(1)
  const currentPageSize = ref(pageSize)
  const fullData = ref([])

  const pagedData = computed(() => {
    const start = (currentPage.value - 1) * currentPageSize.value
    const end = start + currentPageSize.value
    return fullData.value.slice(start, end)
  })

  const total = computed(() => fullData.value.length)

  const updateData = (data) => {
    fullData.value = data || []
    currentPage.value = 1
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
