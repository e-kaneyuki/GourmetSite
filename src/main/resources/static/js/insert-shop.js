//htmlを読み込み終わった後に用意する関数を作成
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.saveShopButton').forEach(button => {
		//クリックした時の関数を作成
        button.addEventListener('click', function(event) {
			//https://qiita.com/yokoto/items/27c56ebc4b818167ef9e
			//e.preventDefaultメソッドを呼び出すことで、このデフォルトの動作をキャンセルしています。
			//つまりhtmlに書いているbutton本来の処理を止める記載。
            event.preventDefault();
            
            // data-form-id属性からフォームのIDを取得
            var formId = this.getAttribute('data-form-id');
            // フォームのIdからフォームの値全てを取得する(例id="form15"でinput name="shopName"のvalue, input name="shopNameKana"のvalue など)
            var form = document.getElementById(formId);
            console.log('form:', form);
            // https://developer.mozilla.org/ja/docs/Web/API/FormData/FormData
            // form のinputのnameプロパティをキーとして値をセットする。今回の場合はth:valueが値となる。
            var formData = new FormData(form);
			console.log('formData:', formData);
            // Fetch APIを使用してフォームデータを非同期に送信
			// https://apidog.com/jp/blog/javascript-fetch/
			// formのaction属性にはth:action="@{/saveStore}"を設定している      
            fetch(form.action, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if(response.ok) {
                    return response.text();
                }
                throw new Error('Network response was not ok.');
            })
            .then(data => {
				alert('登録完了');
                console.log('Success:', data);
            })
            .catch(error => {
				alert('登録失敗');
                console.error('Error:', error);
            });
        });
    });
});
