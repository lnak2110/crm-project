$(document).ready(function() {
    $('.update-user-form').submit(function(e) {
        e.preventDefault();

        let thisForm = $(this)
        let formData = thisForm.serializeArray();
        let data = {}

        $(formData).each(function(_index, item) {
            data[item.name] = item.value;
        });

        $.ajax({
            method: 'PUT',
            url: thisForm.attr('action'),
            contentType: 'application/json',
            data: JSON.stringify(data)
        })
            .always((result) => result.message && alert(result.message))
    })
})