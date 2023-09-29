$(document).ready(function() {
    let searchParams = new URLSearchParams(window.location.search)

    if (!searchParams.has("id") || !searchParams.has("pid")) {
        return;
    }

    let idUser = searchParams.get("id")
    let idProject = searchParams.get("pid")

    renderUserPercents(idUser, idProject)

    renderUsersDetail(idUser, idProject)
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

function renderUserPercents(idUser, idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/` +
            `statuses/percents/user-in-project?uid=${idUser}&pid=${idProject}`,
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
                          <div class="col-xs-12">
                            <h3 class="counter text-right m-t-15 
                            text-${renderPercentsStyle(id).color}">
                              ${percent}%
                            </h3>
                          </div>
                          <div class="col-xs-12">
                            <i data-icon="${renderPercentsStyle(id).dataIcon}" 
                            class="linea-icon linea-basic"></i>
                            <h5 class="text-muted vb text-center">${name}</h5>
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

function renderUsersDetail(idUser, idProject) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/users/detail?id=${idUser}&pid=${idProject}`,
    })
        .done(result => {
            if (result.data) {
                let { fullName, email, tasks } = result.data

                $('.user-content h4').html(fullName)
                $('.user-content h5').html(email)
                $('.tasks').html(renderTasks(tasks))
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
        // Push the task to the corresponding array
        tasksByStatus[statusId].push(task);
    }

    // Create an empty string to store the HTML output
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