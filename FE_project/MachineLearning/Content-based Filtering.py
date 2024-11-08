import pyodbc
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from flask import Flask,jsonify,request
import uuid
from flask_cors import CORS  

app = Flask(__name__)
CORS(app)  # kích hoạt CORS

server = 'localhost'  
database = 'webbanbanhngot'
username = 'root'
password = '472004'  
connection_string = f'DRIVER={{MySQL ODBC 9.1 Unicode Driver}}; SERVER={server}; DATABASE={database}; UID={username}; PWD={password}'

try:
    conn = pyodbc.connect(connection_string)
    query = 'SELECT * FROM product' 

    df_sanPham = pd.read_sql(query, conn)
    #print(df_sanPham.head())

except pyodbc.Error as e:
    print(f'Error: {e}')
finally:
    if 'conn' in locals() and conn:
        conn.close()

features = ['description', 'price']
def combineFeatures(row):
    return str(row['price']) + " " + str(row['description'])
df_sanPham['combinedFutures'] = df_sanPham.apply(combineFeatures,axis=1)
print(df_sanPham['combinedFutures'].head())

tf = TfidfVectorizer()

tfMatrix = tf.fit_transform(df_sanPham['combinedFutures'])

similar = cosine_similarity(tfMatrix)
number = 5

@app.route('/api', methods=['GET'])
def get_data():
    ket_qua = []
    product_id_str = request.args.get('id')
    
    try:
       # Chuyển đổi product_id_str thành UUID và lấy dạng binary để so sánh
       productId = uuid.UUID(product_id_str)
       received_id_binary = productId.bytes

    except ValueError:
        return jsonify({'error': 'ID không hợp lệ'}), 400

    # Kiểm tra xem ID có tồn tại trong dữ liệu hay không
    if received_id_binary not in df_sanPham['id'].values:
        return jsonify({'error': 'ID không tồn tại trong dữ liệu'}), 404

    # Lấy index của sản phẩm theo ID
    indexProduct = df_sanPham[df_sanPham['id'] == received_id_binary].index[0]
    
    # Tìm danh sách các sản phẩm tương tự
    similarProduct = list(enumerate(similar[indexProduct]))
    sortedSimilarProduct = sorted(similarProduct, key=lambda x: x[1], reverse=True)

    # Hàm lấy toàn bộ thông tin sản phẩm từ index của DataFrame
    def lay_thong_tin_san_pham(index):
        san_pham = df_sanPham.iloc[index].to_dict()  # Chuyển dòng DataFrame thành từ điển
        for key, value in san_pham.items():
            if isinstance(value, bytes):
                try:
                # Giữ nguyên UUID ở dạng chuẩn
                    san_pham[key] = str(uuid.UUID(bytes=value))
                except ValueError:
                # Nếu không phải là UUID hợp lệ, chỉ chuyển thành chuỗi hex
                    san_pham[key] = value.hex()
            elif isinstance(value, uuid.UUID):
                san_pham[key] = str(value)  # Chuyển UUID thành chuỗi giữ nguyên dấu gạch ngang
        return san_pham



    for i in range(1, number + 1):
        # print(lay_thong_tin_san_pham(sortedSimilarProduct[i][0]))
        ket_qua.append(lay_thong_tin_san_pham(sortedSimilarProduct[i][0]))

    data = {'san_pham_goi_y': ket_qua}
    return jsonify(data)


if __name__ ==  '__main__':
    app.run(port=5555)
