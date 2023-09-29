export function checkValidDate() {
    let startDate = $('#startDate').val()
    let endDate = $('#endDate').val()

    if (!startDate || !endDate) {
        return true
    }

    let startValue = (new Date(startDate)).getTime();
    let endValue = (new Date(endDate)).getTime();

    return endValue > startValue
}