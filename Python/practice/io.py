import os
print("目前目錄"+ os.getcwd())
if os.path.exists('./practice'):
    print("修改目錄")
    os.chdir('./practice')
else:
    print("沒有practice目錄")

print("目前目錄"+ os.getcwd())
print("列出目錄"+ str(os.listdir()))

#write file
abcFile = open('abc.txt','w')
abcFile.write("abc")
abcFile.close()

#read file
abcFile = open('abc.txt','r')
content = abcFile.read()
print(content)
abcFile.close()

#append file
abcFile = open('abc.txt','w')
abcFile.write("def")
abcFile.close()

#read file
abcFile = open('abc.txt','r')
content = abcFile.read()
print(content)
abcFile.close()