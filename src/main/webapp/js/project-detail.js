$(document).ready(function() {
    let searchParams = new URLSearchParams(window.location.search)

    if (!searchParams.has("id")) {
        return;
    }

    let idProject = searchParams.get("id")

    renderUsersOutsideProject(idProject)

    renderProjectPercents(idProject)

    renderTasksWithNoUser(idProject)

    renderUsersInProject(idProject)

    $('.tasks-users').on('click', '.btn-delete', function() {
        let thisDeleteBtn = $(this)
        let idUser = thisDeleteBtn.attr('data-idUser')

        if (!window.confirm
            (`Are you sure to delete the user with id ${idUser} out of this project?`)) {
            return
        }

        $.ajax({
            method: 'DELETE',
            url: `http://localhost:8080/crm_project_22/api/users/in-project?id=${idUser}&pid=${idProject}`,
        })
            .always((result) => result.message && alert(result.message))
            .done(result => {
                if (result.data) {
                    thisDeleteBtn.closest('.tasks-user').remove();

                    $('#add-members-select').multipleSelect('destroy')
                    renderUsersOutsideProject(idProject)

                    renderTasksWithNoUser(idProject)

                    renderProjectPercents(idProject)
                }
            })
    })

    $('.add-members-form').submit(function(e) {
        e.preventDefault();

        let newMembers = $('#add-members-select').multipleSelect('getSelects')

        $.ajax({
            method: 'PUT',
            url: `http://localhost:8080/crm_project_22/api/projects/add-members?id=${idProject}`,
            contentType: 'application/json',
            data: JSON.stringify({ newMembers })
        })
            .always((result) => result.message && alert(result.message))
            .done(result => {
                if (result.data) {
                    $('#add-members-select').multipleSelect('destroy')
                    renderUsersOutsideProject(idProject)

                    renderUsersInProject(idProject)
                }
            })
    })
})


function renderPercentsStyle(statusId) {
    let style = { dataIcon: '', color: '' }

    switch (statusId) {
        case 1:
            style = { dataIcon: 'E', color: 'danger' }
            break

        case 2:
            style = { dataIcon: '&#xe01b;', color: 'megna' }
            break

        case 3:
            style = { dataIcon: '&#xe00b;', color: 'primary' }
            break

        default:
            break
    }

    return style
}

function renderProjectPercents(idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/statuses/percents/in-project?pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let percentsData = ''

                $.each(result.data, (_index, item) => {
                    let { id, name, percent } = item
                    
                    percentsData += `
                    <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
                      <div class="white-box">
                        <div class="col-in row">
                          <div class="col-md-6 col-sm-6 col-xs-6">
                            <i data-icon="${renderPercentsStyle(item.id).dataIcon}" 
                            class="linea-icon linea-basic"></i>
                            <h5 class="text-muted vb">${name}</h5>
                          </div>
                          <div class="col-md-6 col-sm-6 col-xs-6">
                            <h3 class="counter text-right m-t-15 
                            text-${renderPercentsStyle(id).color}">
                              ${percent}%
                            </h3>
                          </div>
                          <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="progress">
                              <div class="progress-bar progress-bar-${renderPercentsStyle(id).color}" 
                              role="progressbar" aria-valuenow="${percent}" aria-valuemin="0" 
                              aria-valuemax="100" style="width: ${percent}%">
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    `
                })

                $('.percents').html(percentsData)
            }
        })
}

function renderUsersInProject(idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/users/in-project?pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let usersData = ''

                $.each(result.data, (_index, item) => {
                    usersData += `
                    <div class="row tasks-user">
                      <div class="col-xs-12">
                        <div class="group-title" style="justify-content: space-between;">
                          <a href="/crm_project_22/user-detail?id=${item.id}&pid=${idProject}">
                            <img width="30" src="plugins/images/users/pawandeep.jpg" 
                            class="img-circle" />
                            <span>${item.fullName} - ${item.email}</span>
                          </a>
                          <button class='btn btn-sm btn-danger btn-delete'
                            data-idUser='${item.id}'>
                            Delete
                          </button>
                        </div>
                      </div>
                      <div style="width: 100%;">
                        ${renderTasks(item.tasks)}
                      </div>
                    </div>
                    `
                })

                $('.tasks-users').html(usersData)
            }
        })
}

function renderTasksStyle(statusId, firstColumnExists) {
    let style = ''
    
    switch (+statusId) {
        case 2:
            style = `style="left: ${firstColumnExists ? '0' : '33.3%'};"`
            break

        case 3:
            style = 'style="float: right;"'
            break

        default:
            break
    }

    return style
}

function renderTasksWithNoUser(idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/tasks/no-user?pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let tasksData = `
                    <div class="row">
                      <div class="col-xs-12">
                        <div class="group-title">
                          <span>Tasks with no user</span>
                        </div>
                      </div>
                      ${renderTasks(result.data)}
                    </div>
                    `

                $('.tasks-no-user').html(tasksData)
            }
        })
}

function renderTasks(tasks) {
    if (tasks.length === 0) {
        return `
        <div class="col-md-4" style="width: 100%; text-align: center;">
          <div class="white-box">No task assigned yet</div>
        </div>
        `
    }

    // Create an empty object to store the tasks by status
    let tasksByStatus = {};

    // Loop through the tasks array and group them by status id
    for (let task of tasks) {
        let statusId = task.status.id;
        // If the status id is not in the object, create a new array for it
        if (!tasksByStatus[statusId]) {
            tasksByStatus[statusId] = [];
        }

        tasksByStatus[statusId].push(task);
    }

    let html = "";

    let firstColumnExists = false

    // Loop through the object keys, which are the status ids
    for (let key of Object.keys(tasksByStatus)) {
        // Get the status name from the first task in the array
        let statusName = tasksByStatus[key][0].status.name;

        // Start a new div element with a h3 element for the status name
        html += `
        <div class="col-md-4" ${renderTasksStyle(key, firstColumnExists)}>
          <div class="white-box">
            <h3 class="box-title">${statusName}</h3>
            <div class="message-center">`;

        // Loop through the tasks in the array and create an element for each one
        for (let task of tasksByStatus[key]) {
            html += `
            <a href="/crm_project_22/update-task?id=${task.id}">
              <div class="mail-contnet">
                <h5>${task.name}</h5>
                <span class="mail-desc">${task.description}</span>
                <span class="time">
                  From:
                  ${task.startDate ? dayjs(task.startDate).format('DD/MM/YYYY') : 'Unknown date'}
                </span>
                <span class="time">
                  To:
                  ${task.endDate ? dayjs(task.endDate).format('DD/MM/YYYY') : 'Unknown date'}
                </span>
              </div>
            </a>`;
        }

        // Close the div elements for the status and its tasks
        html += `</div></div></div>`;

        firstColumnExists = key == 1
    }


    return html;
}

function renderUsersOutsideProject(idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/users/outside?pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let selectData = ''

                $.each(result.data, (_index, item) => {
                    selectData += `
                    <option value=${item.id}>${item.fullName} - ${item.email}</option>
                    `
                })

                $('#add-members-select').html(selectData)

                $('#add-members-select').multipleSelect({
                    placeholder: 'Users not in this project',
                    filter: true,
                    filterPlaceholder: 'Search by name or email',
                    showClear: true,
                    styler: function(_row) {
                        return 'padding-top: 10px;'
                    },
                    textTemplate: function($el) {
                        return `<span style='margin-left: 5px'>${$el.html()}</span>`
                    },
                })
            }
        })
}