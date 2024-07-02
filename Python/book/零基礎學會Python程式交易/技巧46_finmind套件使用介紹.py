# -*- coding: utf-8 -*-
"""

金融歷史資料套件應用
finmind ：國內金融產品資料取得

# pip install FinMind 

"""

# 載入套件
from FinMind.data import DataLoader

api = DataLoader()

# 載入商品名稱
symbol = "2330"

api.login_by_token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRlIjoiMjAyMy0xMS0wMSAyMjo0MToyMiIsInVzZXJfaWQiOiJ3aWxseTUwNDE0eiIsImlwIjoiMTIzLjE5My4xODAuMjE0In0.pEEWzYeJwYpNPakjnQSyFUOwUx9YecNgCJ6MmX4oaYE")

# 取得歷史資料
df = api.taiwan_stock_daily(
    start_date='2020-04-02',
    end_date='2020-04-12'
)

print(df)