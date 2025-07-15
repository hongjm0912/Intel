// Mock 데이터로 테스트하는 버전
const API_BASE_URL = "/gesipan"

// Mock 데이터
const mockPosts = [
    { id: 1, title: "첫 번째 게시글", content: "첫 번째 게시글의 내용입니다." },
    { id: 2, title: "두 번째 게시글", content: "두 번째 게시글의 내용입니다." },
    { id: 3, title: "세 번째 게시글", content: "세 번째 게시글의 내용입니다." },
]

let nextId = 4

// 현재 상태
let currentPostId = null
let currentView = "list"

// DOM 요소들
const elements = {
    // Views
    listView: document.getElementById("listView"),
    writeView: document.getElementById("writeView"),
    detailView: document.getElementById("detailView"),
    editView: document.getElementById("editView"),
    loading: document.getElementById("loading"),

    // Buttons
    writeBtn: document.getElementById("writeBtn"),
    backToListBtn: document.getElementById("backToListBtn"),
    backToListFromDetailBtn: document.getElementById("backToListFromDetailBtn"),
    backToDetailBtn: document.getElementById("backToDetailBtn"),
    cancelWriteBtn: document.getElementById("cancelWriteBtn"),
    cancelEditBtn: document.getElementById("cancelEditBtn"),
    editBtn: document.getElementById("editBtn"),
    deleteBtn: document.getElementById("deleteBtn"),

    // Forms
    writeForm: document.getElementById("writeForm"),
    editForm: document.getElementById("editForm"),

    // Content areas
    postList: document.getElementById("postList"),
    detailTitle: document.getElementById("detailTitle"),
    detailContent: document.getElementById("detailContent"),

    // Form inputs
    writeTitle: document.getElementById("writeTitle"),
    writeContent: document.getElementById("writeContent"),
    editTitle: document.getElementById("editTitle"),
    editContent: document.getElementById("editContent"),
}

// API 함수들 - Mock 버전
const api = {
    async getPostList() {
        console.log("Mock API: 게시글 목록 조회")

        // 실제 API 호출 시도
        try {
            const response = await fetch(`${API_BASE_URL}/list`)
            if (response.ok) {
                const data = await response.json()
                console.log("실제 API 성공:", data)
                return data
            }
        } catch (error) {
            console.log("실제 API 실패, Mock 데이터 사용:", error.message)
        }

        // Mock 데이터 반환
        return mockPosts.map((post) => ({ id: post.id, title: post.title }))
    },

    async getPost(id) {
        console.log("Mock API: 게시글 상세 조회, ID:", id)

        // 실제 API 호출 시도
        try {
            const response = await fetch(`${API_BASE_URL}/post/${id}`)
            if (response.ok) {
                const data = await response.json()
                console.log("실제 API 성공:", data)
                return { id, ...data }
            }
        } catch (error) {
            console.log("실제 API 실패, Mock 데이터 사용:", error.message)
        }

        // Mock 데이터에서 찾기
        const post = mockPosts.find((p) => p.id === id)
        if (!post) throw new Error("게시글을 찾을 수 없습니다.")
        return post
    },

    async createPost(data) {
        console.log("Mock API: 게시글 작성", data)

        // 실제 API 호출 시도
        try {
            const response = await fetch(`${API_BASE_URL}/posting`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            })
            if (response.ok) {
                console.log("실제 API 성공")
                return response.text()
            }
        } catch (error) {
            console.log("실제 API 실패, Mock 데이터 사용:", error.message)
        }

        // Mock 데이터에 추가
        const newPost = { id: nextId++, ...data }
        mockPosts.push(newPost)
        return "작성 완료"
    },

    async getPostForUpdate(id) {
        console.log("Mock API: 수정용 게시글 조회, ID:", id)

        // 실제 API 호출 시도
        try {
            const response = await fetch(`${API_BASE_URL}/update/${id}`)
            if (response.ok) {
                const data = await response.json()
                console.log("실제 API 성공:", data)
                return data
            }
        } catch (error) {
            console.log("실제 API 실패, Mock 데이터 사용:", error.message)
        }

        // Mock 데이터에서 찾기
        const post = mockPosts.find((p) => p.id === id)
        if (!post) throw new Error("게시글을 찾을 수 없습니다.")
        return { title: post.title, content: post.content }
    },

    async updatePost(id, data) {
        console.log("Mock API: 게시글 수정", id, data)

        // 실제 API 호출 시도
        try {
            const response = await fetch(`${API_BASE_URL}/update/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            })
            if (response.ok) {
                console.log("실제 API 성공")
                return response.text()
            }
        } catch (error) {
            console.log("실제 API 실패, Mock 데이터 사용:", error.message)
        }

        // Mock 데이터 수정
        const post = mockPosts.find((p) => p.id === id)
        if (post) {
            post.title = data.title
            post.content = data.content
        }
        return "수정 완료"
    },

    async deletePost(title) {
        console.log("Mock API: 게시글 삭제", title)

        // 실제 API 호출 시도
        try {
            const response = await fetch(`${API_BASE_URL}/delete?title=${encodeURIComponent(title)}`, {
                method: "DELETE",
            })
            if (response.ok) {
                console.log("실제 API 성공")
                return response.text()
            }
        } catch (error) {
            console.log("실제 API 실패, Mock 데이터 사용:", error.message)
        }

        // Mock 데이터에서 삭제
        const index = mockPosts.findIndex((p) => p.title === title)
        if (index > -1) {
            mockPosts.splice(index, 1)
        }
        return "삭제 완료"
    },
}

// 유틸리티 함수들
function showLoading() {
    elements.loading.classList.remove("hidden")
}

function hideLoading() {
    elements.loading.classList.add("hidden")
}

function showView(viewName) {
    // 모든 뷰 숨기기
    document.querySelectorAll(".view").forEach((view) => {
        view.classList.add("hidden")
    })

    // 선택된 뷰 보이기
    const viewElement = elements[viewName + "View"]
    if (viewElement) {
        viewElement.classList.remove("hidden")
        currentView = viewName
    }
}

function showMessage(message, type = "success") {
    const messageDiv = document.createElement("div")
    messageDiv.className = `${type}-message`
    messageDiv.textContent = message

    const container = document.querySelector(".container")
    container.insertBefore(messageDiv, container.firstChild)

    setTimeout(() => {
        messageDiv.remove()
    }, 3000)
}

function showError(message) {
    showMessage(message, "error")
}

// 게시글 목록 관련 함수들
async function loadPostList() {
    try {
        showLoading()
        const posts = await api.getPostList()
        renderPostList(posts)
    } catch (error) {
        showError(error.message)
        renderEmptyState()
    } finally {
        hideLoading()
    }
}

function renderPostList(posts) {
    if (posts.length === 0) {
        renderEmptyState()
        return
    }

    const html = posts
        .map(
            (post) => `
        <div class="post-item" onclick="viewPost(${post.id})">
            <div class="post-title">${escapeHtml(post.title)}</div>
            <div class="post-id">#${post.id}</div>
        </div>
    `,
        )
        .join("")

    elements.postList.innerHTML = html
}

function renderEmptyState() {
    elements.postList.innerHTML = `
        <div class="empty-state">
            <p>게시글이 없습니다.</p>
        </div>
    `
}

// 게시글 상세보기 관련 함수들
async function viewPost(id) {
    try {
        showLoading()
        currentPostId = id
        const post = await api.getPost(id)
        renderPostDetail(post)
        showView("detail")
    } catch (error) {
        showError(error.message)
    } finally {
        hideLoading()
    }
}

function renderPostDetail(post) {
    elements.detailTitle.textContent = post.title
    elements.detailContent.textContent = post.content
}

// 게시글 작성 관련 함수들
function showWriteForm() {
    elements.writeTitle.value = ""
    elements.writeContent.value = ""
    showView("write")
}

async function submitPost(event) {
    event.preventDefault()

    const title = elements.writeTitle.value.trim()
    const content = elements.writeContent.value.trim()

    if (!title || !content) {
        showError("제목과 내용을 모두 입력해주세요.")
        return
    }

    try {
        showLoading()
        await api.createPost({ title, content })
        showMessage("게시글이 작성되었습니다.")
        showView("list")
        loadPostList()
    } catch (error) {
        showError(error.message)
    } finally {
        hideLoading()
    }
}

// 게시글 수정 관련 함수들
async function showEditForm() {
    try {
        showLoading()
        const post = await api.getPostForUpdate(currentPostId)
        elements.editTitle.value = post.title
        elements.editContent.value = post.content
        showView("edit")
    } catch (error) {
        showError(error.message)
    } finally {
        hideLoading()
    }
}

async function submitEdit(event) {
    event.preventDefault()

    const title = elements.editTitle.value.trim()
    const content = elements.editContent.value.trim()

    if (!title || !content) {
        showError("제목과 내용을 모두 입력해주세요.")
        return
    }

    try {
        showLoading()
        await api.updatePost(currentPostId, { title, content })
        showMessage("게시글이 수정되었습니다.")
        viewPost(currentPostId)
    } catch (error) {
        showError(error.message)
    } finally {
        hideLoading()
    }
}

// 게시글 삭제 관련 함수들
async function deletePost() {
    if (!confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
        return
    }

    try {
        showLoading()
        const post = await api.getPost(currentPostId)
        await api.deletePost(post.title)
        showMessage("게시글이 삭제되었습니다.")
        showView("list")
        loadPostList()
    } catch (error) {
        showError(error.message)
    } finally {
        hideLoading()
    }
}

// HTML 이스케이프 함수
function escapeHtml(text) {
    const div = document.createElement("div")
    div.textContent = text
    return div.innerHTML
}

// 이벤트 리스너 등록
function initEventListeners() {
    // 네비게이션 버튼들
    elements.writeBtn.addEventListener("click", showWriteForm)
    elements.backToListBtn.addEventListener("click", () => {
        showView("list")
        loadPostList()
    })
    elements.backToListFromDetailBtn.addEventListener("click", () => {
        showView("list")
        loadPostList()
    })
    elements.backToDetailBtn.addEventListener("click", () => viewPost(currentPostId))

    // 취소 버튼들
    elements.cancelWriteBtn.addEventListener("click", () => {
        showView("list")
        loadPostList()
    })
    elements.cancelEditBtn.addEventListener("click", () => viewPost(currentPostId))

    // 액션 버튼들
    elements.editBtn.addEventListener("click", showEditForm)
    elements.deleteBtn.addEventListener("click", deletePost)

    // 폼 제출
    elements.writeForm.addEventListener("submit", submitPost)
    elements.editForm.addEventListener("submit", submitEdit)
}

// 전역 함수 (HTML에서 호출)
window.viewPost = viewPost

// 초기화
document.addEventListener("DOMContentLoaded", () => {
    initEventListeners()
    loadPostList()
})
