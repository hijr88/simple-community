'use strict'

async function changeRole(id) { //권한 변경할 회원 아이디
    let role = document.querySelector('input[name = role]:checked').value;
    role = 'ROLE_' + role;

    const response = await fetch(`${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({'role': role, 'type': 'role'})
    });
    if (!response.ok) {
        alert('Server Error');
        return;
    }
    const text = await response.text();
    if (text === '1') showAlert('success', '권한이 변경되었습니다.', true);
    else showAlert('danger', '다시 시도해주세요.', true);
}

async function changeEnable(id) { //활동 변경할 회원 아이디
    const enable = document.querySelector('input[name = enable]:checked').value;

    const response = await fetch(`${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({'enable': enable, 'type': 'enable'})
    });
    if (!response.ok) {
        alert('Server Error');
        return;
    }
    const text = await response.text();
    if (text === '1') showAlert('success', '활동이 변경되었습니다.', true);
    else showAlert('danger', '다시 시도해주세요.', true);
}

async function toggleBoardPub(postId, button) { //글번호, 비/공개 버튼
    const value = button.value;
    const pub = value === '공개하기';

    if (confirm((pub ? '공개' : '비공개') + ' 하시겠습니까?')) {
        const response = await fetch(`/api/posts/${postId}/edit/pub`, {
            method: 'PUT',
            headers: {'Content-Type': 'text/plain'},
            body: pub
        });
        if (!response.ok) {
            alert('Server Error!!')
            return;
        }

        const text = await response.text();
        if (text === 'true') {
            button.value = (pub ? '비공개하기' : '공개하기');
            showAlert('success', '변경되었습니다.', true);
        } else
            alert('다시 시도 해주세요.');
    }
}

async function toggleGalleryPub(gno, button) { //글번호, 비/공개 버튼
    const value = button.value;
    const pub = value === '공개하기';

    if (confirm((pub ? '공개' : '비공개') + ' 하시겠습니까?')) {
        const response = await fetch(`${getRoot()}/api/galleries/${gno}/edit/pub`, {
            method: 'PUT',
            headers: {'Content-Type': 'text/plain'},
            body: pub
        });
        if (!response.ok) {
            alert('Server Error!!')
            return;
        }

        const text = await response.text();
        if (text === 'true') {
            button.value = (pub ? '비공개하기' : '공개하기');
            showAlert('success', '변경되었습니다.', true);
        } else
            alert('다시 시도 해주세요.');

    }
}