function deleteProduct(id) {
fetch('/products/' + id, {
    method: 'DELETE'
})
    .then(response => {
        if (response.ok) {
            location.reload();
        } else {
            alert('Delete failed!');
        }
    })
    .catch(err => console.error(err));
}