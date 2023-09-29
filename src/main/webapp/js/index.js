$(document).ready(function() {
    // idUser, roleUser from jsp

    renderPercents(idUser, roleUser.toUpperCase())
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

function renderPercents(idUser, roleUser) {
    let roles = ['ADMIN', 'LEADER']
    let isLeaderAbove = roles.indexOf(roleUser) > -1

    $.ajax({
        method: 'GET',
        url: isLeaderAbove ?
            `http://localhost:8080/crm_project_22/api/statuses/percents/leader?uid=${idUser}` :
            `http://localhost:8080/crm_project_22/api/statuses/percents/user?uid=${idUser}`,
    })
        .done(result => {
            if (result.data) {
                let percentsData = ''

                $.each(result.data, (_index, item) => {
                    let { id, name, percent, count } = item

                    percentsData += `
                    <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
                      <div class="white-box">
                        <div class="col-in row">
                          <div class="col-md-6 col-sm-6 col-xs-6">
                            <i data-icon="${renderPercentsStyle(id).dataIcon}" 
                            class="linea-icon linea-basic"></i>
                            <h5 class="text-muted vb text-center">${name}</h5>
                          </div>
                          <div class="col-md-6 col-sm-6 col-xs-6">
                            <h3 class="counter text-right m-t-15 
                            text-${renderPercentsStyle(id).color}">
                              ${count}
                            </h3>
                          </div>
                          <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="progress">
                              <div class="progress-bar progress-bar-${renderPercentsStyle(id).color}" 
                              role="progressbar" aria-valuenow="${percent}" aria-valuemin="0" 
                              aria-valuemax="100" style="width: ${percent}%">
                              </div>
                            </div>
                            <span>
                              ${percent}% of total ${isLeaderAbove ? 'projects' : 'tasks'}
                            </span>
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
