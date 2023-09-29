$(document).ready(function() {
    renderTasksTable()

    $('.tasks-table').on('click', '.btn-delete', function() {
        let idTask = $(this).attr('data-idTask')

        if (!window.confirm(`Are you sure to delete the task with id ${idTask}?`)) {
            return
        }

        $.ajax({
            method: 'DELETE',
            url: `http://localhost:8080/crm_project_22/api/tasks?id=${idTask}`,
        })
            .always((result) => result.message && alert(result.message))
            .done(result => {
                if (result.data) {
                    $('.tasks-table').DataTable().row($(this).closest('tr')).remove().draw();

                    resetCounter()
                }
            })
    })
})


function renderTasksTable() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/crm_project_22/api/tasks',
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
                      <td>${item.description || ''}</td>
                      <td>${item.user.email || ''}</td>
                      <td>
                        ${item.startDate ? dayjs(item.startDate).format('DD/MM/YYYY') : ''}
                      </td> 
                      <td>
                        ${item.endDate ? dayjs(item.endDate).format('DD/MM/YYYY') : ''}
                      </td> 
                      <td>${item.status.name}</td>
                      <td>
                        <a href='/crm_project_22/update-task?id=${item.id}' 
                        class='btn btn-sm btn-primary'>
                          Sửa
                        </a>
                        <a href='javascript:void(0);' class='btn btn-sm btn-danger btn-delete'
                          data-idTask='${item.id}'>
                          Xóa
                        </a>
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