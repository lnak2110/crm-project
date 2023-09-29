$(document).ready(function() {
    let searchParams = new URLSearchParams(window.location.search)

    if (!searchParams.has("id")) {
        return;
    }

    let idUser = searchParams.get("id")

    renderUserPercents(idUser)

    renderUsersProfile(idUser)

    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/crm_project_22/api/statuses',
    })
        .done(result => {
            if (result.data) {
                renderTasksTable(result.data, idUser)
            }
        })

    $('.tasks-table').on('click', '.btn-update-status', function() {
        let idTask = $(this).attr('data-idTask')

        updateStatus(idTask, idUser)
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

function renderUserPercents(idUser) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/statuses/percents/user?uid=${idUser}`,
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

function renderUsersProfile(idUser) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/users/profile?id=${idUser}`,
    })
        .done(result => {
            if (result.data) {
                let { fullName, email } = result.data

                $('.user-content h4').html(fullName)
                $('.user-content h5').html(email)
            }
        })
}

function renderStatuses(statuses, idStatus) {
    let statusesData = ''

    $.each(statuses, (_index, item) => {
        statusesData += `
        <option value=${item.id} ${item.id == idStatus ? 'selected' : ''}>
          ${item.name}
        </option>
        `
    })

    return statusesData
}

function updateStatus(idTask, idUser) {
    let idStatus = $(`.status-select[data-idTask=${idTask}]`).val()
    let nameStatus = $(`.status-select[data-idTask=${idTask}] :selected`).text().trim();

    if (!window.confirm(`Are you sure to update the task with id ${idTask} to '${nameStatus}'?`)) {
        return
    }

    $.ajax({
        method: 'PUT',
        url: `http://localhost:8080/crm_project_22/api/` +
            `tasks/update-status?id=${idTask}&sid=${idStatus}`,
    })
        .always((result) => result.message && alert(result.message))
        .done(result => {
            result.data && renderUserPercents(idUser)
        })
}

function renderTasksTable(statuses, idUser) {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/crm_project_22/api/tasks/user?uid=${idUser}`,
    })
        .done(result => {
            if (result.data) {
                let tableData = ''

                $.each(result.data, (_index, item) => {
                    tableData += `
                    <tr>
                      <td></td>
                      <td>${item.id}</td>
                      <td>${item.name}</td>
                      <td>${item.project.id} - ${item.project.name}</td>
                      <td>${item.project.user.email}</td>
                      <td>${item.description || ''}</td>
                      <td>
                        ${item.startDate ? dayjs(item.startDate).format('DD/MM/YYYY') : ''}
                      </td> 
                      <td>
                        ${item.endDate ? dayjs(item.endDate).format('DD/MM/YYYY') : ''}
                      </td> 
                      <td>
                        <select class='status-select' data-idTask='${item.id}'>
                          ${renderStatuses(statuses, item.status.id)}
                        </select>  
                      </td>
                      <td>
                        <button class='btn btn-sm btn-primary btn-update-status' 
                        data-idTask='${item.id}'>
                          Update Status
                        </button>
                      </td>
                    </tr>
                    `
                })

                $('.tasks-table tbody').html(tableData)

                resetCounter()
            }
        })
}

// Reset index each row after delete
function resetCounter() {
    let table = $('.tasks-table').DataTable({
        retrieve: true,
        columnDefs: [
            {
                searchable: false,
                orderable: false,
                targets: 0
            }
        ],
        order: [[1, 'asc']]
    })

    table.on('order.dt search.dt', function() {
        let i = 1;

        table
            .cells(null, 0, { search: 'applied', order: 'applied' })
            .every(function(_cell) {
                this.data(i++);
            });
    }).draw();
}