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
		
		//Get person detail
		function getPersonDetail(id) {
			$.ajax({
				url: `/bin/searchPerson?idPerson=${id}`,
				type: 'GET',
				dataType: 'json',
				contentType: 'application/json',
				success: function(responseData) {
					$("#name").val(responseData.result.name)
					$(`input[id='${responseData.result.sex}']`).attr("checked", true)
					$("#birthday").val(responseData.result.birthday)
					$("#edit-id").val(responseData.result.id)
				}
			});
		}

		//Get search person
		function getSearch(searchvalue, currentPage) {
			$.ajax({
				url: `http://localhost:4502/bin/searchPerson?keywork=${searchvalue}&pageNumber=${currentPage}&resultPerPage=2`,
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
						<p>Name: ${result.name}</p>
						<p>Birthday: ${result.birthday}</p>
						<p>Sex: ${result.sex}</p>
						<div>
							<button class="edit-btn" data-id='${result.id}'>Edit</button>
							<button class="delete-btn" data-id='${result.id}'>Delete</button>
						</div>
						
					</li>`
		}

		function renderPage(page, currentPage) {
			return `<button data-page='${page}' id="page-item" class=${page + 1 == currentPage ? 'pagen page-active' : 'pagen'}>${page}</button>`
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

			//Delete Person
			$(".delete-btn").on("click", function() {

				let idPerson = $(this).data("id");
				console.log("id: ", idPerson)
				$.ajax({
					url: `/bin/searchPerson?id=${idPerson}`,
					//url: `/bin/searchPerson`,
					type: "DELETE",
					dataType: "json",
					data: {
						id: idPerson,
					},
					success: function(data) {
						if (data.status == "successed") {
							console.log("Delete succsess")
						} else {
							console.log("Delete Error")
						}
					},
					error: function(data) {
						showMessage('alert alert-danger', 'Server Error');
					}
				});
			})
			
			//Get person detail
			$(".edit-btn").on("click", function() {
			let idPerson = $(this).data("id")
			getPersonDetail(idPerson);
		})
		}
		
		//Add person
		$("#btn-add").on("click", function(e) {
			e.preventDefault();

			let name = $("#name").val()
			let sex = $("input[name='sex']:checked").val()
			let birthday = $("#birthday").val()

			$.ajax({
				url: '/bin/searchPerson',
				type: "POST",
				dataType: "json",
				data: {
					name: name,
					sex: sex,
					birthday: birthday
				},
				success: function(data) {
					if (data.status == "successed") {
						console.log("Add succsess")
					} else {
						console.log("Add Error")
					}
				},
				error: function(data) {
					console.log("Edit Error")
				}
			});
		})
		
		$("#btn-save-edit").on("click", function(e) {
			e.preventDefault();
			
			let name = $("#name").val()
			let sex = $("input[name='sex']:checked").val()
			let birthday = $("#birthday").val()
			let id = $("#edit-id").val()
			
			console.log(name)
			console.log(sex)
			console.log(birthday)
			console.log(id)
			
			$.ajax({
				url: '/bin/searchPerson',
				type: "PUT",
				dataType: "json",
				data: {
					name: name,
					sex: sex,
					birthday: birthday,
					id:id
				},
				success: function(data) {
					if (data.status == "successed") {
						console.log("Edit succsess")
						getSearch($("#searchInput").val(), 1)
					}
				},
				error: function(data) {
					console.log("Edit Error")
				}
			});
		})
	})

}());

