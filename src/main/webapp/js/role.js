$(document).ready(function() {
    renderRolesTable()

    $('.roles-table').on('click', '.btn-delete', function() {
        let idRole = $(this).attr('data-idRole')

        if (!window.confirm(`Are you sure to delete the role with id ${idRole}?`)) {
            return
        }

        $.ajax({
            method: 'DELETE',
            url: `http://localhost:8080/crm_project_22/api/roles?id=${idRole}`,
        })
            .always((result) => result.message && alert(result.message))
            .done(result => {
                if (result.data) {
                    $('.roles-table').DataTable().row($(this).closest('tr')).remove().draw();

                    resetCounter()
                }
            })
    })
})

function renderRolesTable() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/crm_project_22/api/roles',
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
                        <a href='/crm_project_22/update-role?id=${item.id}' 
                        class='btn btn-sm btn-primary'>
                          Update
                        </a>
                        <a href='javascript:void(0);' class='btn btn-sm btn-danger btn-delete'
                          data-idRole='${item.id}'>
                          Delete
                        </a>
                      </td>
                    </tr>
                    `
                })

                $('.roles-table tbody').html(tableData)

                resetCounter()
            }
        })
}

// Reset index each row after delete
function resetCounter() {
    let table = $('.roles-table').DataTable({
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