/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
	document.querySelectorAll('.deleteShopButton').forEach( functionB =>{
		functionB.addEventListener('click', function(event) {
			event.preventDefault();
			
			var formId = this.getAttribute('data-form-id');
			console.log('formId:', formId);
			var form = document.getElementById(formId);
			console.log('form:', form);
			var formData = new FormData(form);

			
//			https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
			fetch(form.action, {
				method: "POST",
				redirect: "follow",
				body: formData
			})
			  .then(response => {
			    if(response.ok) {
                    return response.text();
                }
                throw new Error('Network response was not ok.');
			  })
			  .then(data => {
                document.location='/favorites/list/name';
              })
              .catch(error => {
				alert('削除失敗');
                console.error('Error:', error);
              });
		})
	})
})