print ("hello world")

#for
for i in range(1,10,1):
    print (i)

#while
j=1
while(j<10):
    print('j='+str(j))
    j+=1

#list
langList = ['java','c#','vb','docker','python']
print (langList)
print (langList[1:3]) #適用索引值，類似substring
print (langList[-2:-1])

for lang in langList:
    print(lang)

print('c++' in langList)

print('c++' not in langList)

#print(list.count('java'))
langList.append("abc")


list1 = ['chinese','english']
list1.append('france')



list3 = []
list3.extend(list1)
list3.extend(langList)
print(list3)
list3.sort()
print(list3)
list3Copy = list3.copy()

print(len(list3Copy))

#tuple
tuple1 = (1,2,3)

tl1 = list(tuple1)
tuple2 = tuple(tl1)
print (tuple2)

#Directory
dir = {"a":1, "b":2, "c":3}

print (dir.keys())
print (dir.values())
print (dir.items())
print (dir.get("a"))
print ("a" not in dir.keys())
dir["d"]=4
print (dir.get("d"))

#Set
testSet = {"a","a","b","c","d"}
print(testSet)
print(len(testSet))

#for i in dir.keys()