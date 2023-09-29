$(document).ready(function() {
    $('.create-user-form').submit(function(e) {
        e.preventDefault();

        let thisForm = $(this)

        $.ajax({
            method: 'POST',
            url: thisForm.attr('action'),
            data: thisForm.serialize()
        })
            .done(() => $('.create-user-form')[0].reset())
            .always((result) => result.message && alert(result.message))
    })
})