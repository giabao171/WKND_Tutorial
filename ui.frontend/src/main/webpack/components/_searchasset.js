/*

(function() {
	"use strict";

	$(document).ready(function() {
		let searchValue = $("#searchInput").val();
		$("#searh-btn").on("click", function(event) {
			event.preventDefault();
			searchValue = $("#searchInput").val();
			console.log("Search value: ", searchValue)
			if (searchValue === "") {
				$("#render-sesult").children("ul").empty();
				$("#page").empty();
			}
			else {
				$("#render-sesult").children("ul").empty();
				$("#page").empty();
				getSearch(searchValue, 1);
			}

		})

		function getSearch(searchvalue, currentPage) {
			$.ajax({
				url: `/wknd/search?searchText=${searchvalue}&pageNumber=${currentPage}&resultPerPage=1`,
				type: 'GET',
				dataType: 'json',
				contentType: 'application/json',
				success: function(responseData) {
					console.log("responseData: ", responseData)
					responseData.results.forEach(item => {
						$("#render-sesult").children("ul").append(renderResult(item))
						console.log(item)
					})
					renderListPage(responseData.totalPage, responseData.curentPage)
				}
			});
		}

		function renderResult(result) {
			return `<li>
						<p>Title: ${result.title}</p>
						<p>Path: ${result.path}</p>
					</li>`
		}

		function renderPage(page, currentPage) {
			return `<button data-page='${page}' id="page-item" class=${page == currentPage ? 'page-active' : 'pagen'}>${page}</button>`
		}

		function renderListPage(totalPage, currentPage) {
			let i = 1;
			while (i <= totalPage) {
				$("#page").append(renderPage(i, currentPage))
				++i;
			}

			$(".pagen").on("click", function() {
				$("#render-sesult").children("ul").empty();
				$("#page").empty();

				console.log($(this).data("page"))

				searchValue = $("#searchInput").val();
				getSearch(searchValue, $(this).data("page"))
			})

		}

	})
}());

*/