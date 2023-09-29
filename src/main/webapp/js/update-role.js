$(document).ready(function() {
    $('.update-role-form').submit(function(e) {
        e.preventDefault();

        let thisForm = $(this)
        let name = $('.role-name-input').val()
        let description = $('.role-description-input').val()

        $.ajax({
            method: 'PUT',
            url: thisForm.attr('action'),
            contentType: 'application/json',
            data: JSON.stringify({
                name, description
            })
        })
            .always((result) => result.message && alert(result.message))
    })
})