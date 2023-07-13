toastr.options = {
    closeButton: true,
    debug: false,
    newestOnTop: true,
    progressBar: true,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

function parseMsg(msg) {
    const [pureMsg, ttl] = msg.split(";ttl=");

    const currentJsUnixTimestamp = new Date().getTime();

    if (ttl && parseInt(ttl) + 5000 < currentJsUnixTimestamp) {
        return [pureMsg, false];
    }

    return [pureMsg, true];
}

function toastNotice(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["success"](pureMsg, "알림");
    }
}

function toastWarning(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["warning"](pureMsg, "경고");
    }
}


function showDeleteConfirmDialog(msg, callback) {
    Swal.fire({
        title: '주의',
        text: msg,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '승인',
        cancelButtonText: '취소',
        reverseButtons: true, // 버튼 순서 거꾸로
    }).then((result) => {
        if (result.isConfirmed) {
            callback();
        }
    });
}

function showMovetoMainConfirmDialog(msg, callback) {
    Swal.fire({
        title: '성공',
        text: msg,
        icon: 'success',
        showCancelButton: true,
        confirmButtonColor: '#2e3bc0',
        cancelButtonColor: '#6dd2ce',
        confirmButtonText: '지도로 이동',
        cancelButtonText: '계속 등록',
        reverseButtons: true, // 버튼 순서 거꾸로
    }).then((result) => {
        if (result.isConfirmed) {
            callback();
        }
    });
}
