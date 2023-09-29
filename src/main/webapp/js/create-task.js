import { checkValidDate } from './helpers.js'

$(document).ready(function() {
    let previous;

    renderProjects()

    $('#project-select').on('focus', function() {
        previous = this.value
    }).change(function() {
        if (previous == this.value) {
            return
        }

        previous = this.value

        $('#user-select').multipleSelect('destroy')

        renderUsersInProject(this.value)
    })

    $('.create-task-form').submit(function(e) {
        e.preventDefault();

        if (!checkValidDate()) {
            alert('Start date must be before end date!')
            return
        }

        let thisForm = $(this)

        //let newMembers = $('#add-members-select').multipleSelect('getSelects')

        $.ajax({
            method: 'POST',
            url: thisForm.attr('action'),
            data: thisForm.serialize()
        })
            .done(() => {
                previous = null
                $('.create-task-form')[0].reset()
                refreshSelect()
            })
            .always((result) => result.message && alert(result.message))
    })
})

function renderUsersInProject(idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/users/in-project?pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let selectData = ''

                $.each(result.data, (_index, item) => {
                    selectData += `
                    <option value=${item.id}>${item.fullName} - ${item.email}</option>
                    `
                })

                $('#user-select').html(selectData)

                $('#user-select').multipleSelect({
                    placeholder: 'Member options are based on the selected project above',
                    filter: true,
                    filterPlaceholder: 'Search by name or email'
                })
            }
        })
}

function renderProjects() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/crm_project_22/api/projects',
    })
        .done(result => {
            if (result.data) {
                let selectData = ''

                $.each(result.data, (_index, item) => {
                    selectData += `
                    <option value=${item.id}>${item.id} - ${item.name}</option>
                    `
                })

                $('#project-select').html(selectData)

                $('#project-select').multipleSelect({
                    placeholder: 'Select a project to see all members inside',
                    filter: true,
                    filterPlaceholder: 'Search by id or name'
                })

                renderUsersInProject($('#project-select').val() || 0)
            }
        })
}

function refreshSelect() {
    $('#project-select').multipleSelect('destroy')

    $('#user-select').multipleSelect('destroy')

    $('#project-select').multipleSelect({
        placeholder: 'Select a project to see all members inside',
        filter: true,
        filterPlaceholder: 'Search by id or name'
    })

    renderUsersInProject($('#project-select').val() || 0)
}