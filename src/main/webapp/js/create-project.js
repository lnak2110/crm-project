import { checkValidDate } from './helpers.js'

$(document).ready(function() {
    $('.create-project-form').submit(function(e) {
        e.preventDefault();

        if (!checkValidDate()) {
            alert('Start date must be before end date!')
            return
        }

        let thisForm = $(this)

        $.ajax({
            method: 'POST',
            url: thisForm.attr('action'),
            data: thisForm.serialize()
        })
            .done(() => $('.create-project-form')[0].reset())
            .always((result) => result.message && alert(result.message))
    })
})
