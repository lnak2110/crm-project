$(document).ready(function() {
    renderUsersTable()

    $('.users-table').on('click', '.btn-delete', function() {
        let idUser = $(this).attr('data-idUser')

        if (!window.confirm(`Are you sure to delete the user with id ${idUser}?`)) {
            return
        }

        $.ajax({
            method: 'DELETE',
            url: `http://localhost:8080/crm_project_22/api/users?id=${idUser}`,
        })
            .always((result) => result.message && alert(result.message))
            .done(result => {
                if (result.data) {
                    $('.users-table').DataTable().row($(this).closest('tr')).remove().draw();

                    resetCounter()
                }
            })
    })
})

function renderUsersTable() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/crm_project_22/api/users',
    })
        .done(result => {
            if (result.data) {
                let tableData = ''

                $.each(result.data, (_index, item) => {
                    tableData += `
                    <tr>
                      <td></td>
                      <td>${item.id}</td>
                      <td>${item.email}</td>
                      <td>${item.fullName}</td>
                      <td>${item.phoneNumber || ''}</td>
                      <td>${item.role.name || ''}</td>
                      <td>
                        <a href='/crm_project_22/profile?id=${item.id}' 
                        class="btn btn-sm btn-info">View</a>
                        <a href='/crm_project_22/update-user?id=${item.id}' 
                        class='btn btn-sm btn-primary'>
                          Update
                        </a>
                        <a href='javascript:void(0);' class='btn btn-sm btn-danger btn-delete'
                          data-idUser='${item.id}'>
                          Delete
                        </a>
                      </td>
                    </tr>
                    `
                })

                $('.users-table tbody').html(tableData)

                resetCounter()
            }
        })
}

// Reset index each row after delete
function resetCounter() {
    let table = $('.users-table').DataTable({
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