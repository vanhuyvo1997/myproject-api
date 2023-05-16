package com.myprojectapi.util;

import java.util.List;

public record PageResponse<T>(
		Integer totalPages,
		Integer currentPageNum,
		List<T> currentPageContent)
{
	
}
