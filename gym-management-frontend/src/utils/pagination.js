import { ref, computed } from 'vue'

export function createPagination(pageSize = 30) {
  const currentPage = ref(1)
  const currentPageSize = ref(pageSize)
  const fullData = ref([])
  const total = ref(0)

  const pagedData = computed(() => {
    return fullData.value
  })

  const updateData = (data, totalCount) => {
    fullData.value = data || []
    total.value = totalCount !== undefined ? totalCount : fullData.value.length
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
