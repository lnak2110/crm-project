import { checkValidDate } from './helpers.js'

$(document).ready(function() {
    $('.update-project-form').submit(function(e) {
        e.preventDefault();

        if (!checkValidDate()) {
            alert('Start date must be before end date!')
            return
        }

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