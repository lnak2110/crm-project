import { checkValidDate } from './helpers.js'

$(document).ready(function() {
    let searchParams = new URLSearchParams(window.location.search)

    if (!searchParams.has("id")) {
        return;
    }

    let idTask = searchParams.get("id")

    renderTask(idTask)

    $('.update-task-form').submit(function(e) {
        e.preventDefault();

        if (!checkValidDate()) {
            alert('Start date must be before end date!')
            return
        }

        let thisForm = $(this)
        let formData = new FormData(this);
        let data = { oldIdUser } // oldIdUser from jsp

        for (let key of formData.keys()) {
            if (key.endsWith('[]')) {
                data[key] = formData.getAll(key);
            } else {
                data[key] = formData.get(key);
            }
        }

        // let newMembers = $('#add-members-select').multipleSelect('getSelects')

        $.ajax({
            method: 'PUT',
            url: thisForm.attr('action'),
            contentType: 'application/json',
            data: JSON.stringify(data)
        })
            .always((result) => result.message && alert(result.message))
    })
})

function renderUserSelect(idProject, userId) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/users/in-project?pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let selectData = ''

                $.each(result.data, (_index, item) => {
                    selectData += `
                    <option ${item.id == userId ? 'selected' : ''} value=${item.id}>
                      ${item.fullName} - ${item.email}
                    </option>
                    `
                })

                $('#user-select').html(selectData)

                $('#user-select').multipleSelect({
                    placeholder: 'Task user',
                    filter: true,
                    filterPlaceholder: 'Search by name or email'
                })
            }
        })
}

function renderTask(idTask) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/tasks/detail?id=${idTask}`,
    })
        .done(result => {
            if (result.data) {
                let { project, user } = result.data

                renderUserSelect(project.id, user.id)
            }
        })
}