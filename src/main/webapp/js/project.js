$(document).ready(function() {
    renderProjectsTable()

    $('.projects-table').on('click', '.btn-delete', function() {
        let idProject = $(this).attr('data-idProject')

        if (!window.confirm(`Are you sure to delete the project with id ${idProject}?`)) {
            return
        }

        $.ajax({
            method: 'DELETE',
            url: `http://localhost:8080/crm_project_22/api/projects?id=${idProject}`,
        })
            .always((result) => result.message && alert(result.message))
            .done(result => {
                if (result.data) {
                    $('.projects-table').DataTable().row($(this).closest('tr')).remove().draw();

                    resetCounter()
                }
            })
    })
})

function renderProjectsTable() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/crm_project_22/api/projects',
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
                      <td>${item.description || ''}</td>
                      <td>
                        ${item.startDate ? dayjs(item.startDate).format('DD/MM/YYYY') : ''}
                      </td> 
                      <td>
                        ${item.endDate ? dayjs(item.endDate).format('DD/MM/YYYY') : ''}
                      </td> 
                      <td>${item.user.fullName}</td>
                      <td>${item.status.name}</td>
                      <td>
                        <a href='/crm_project_22/project-detail?id=${item.id}' 
                        class='btn btn-sm btn-info'>
                          View
                        </a>
                        <a href='/crm_project_22/update-project?id=${item.id}' 
                        class='btn btn-sm btn-primary'>
                          Update
                        </a>
                        <a href='javascript:void(0);' class='btn btn-sm btn-danger btn-delete'
                          data-idProject='${item.id}'>
                          Delete
                        </a>
                      </td>
                    </tr>
                    `
                })

                $('.projects-table tbody').html(tableData)

                resetCounter()
            }
        })
}

// Reset index each row after delete
function resetCounter() {
    let table = $('.projects-table').DataTable({
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