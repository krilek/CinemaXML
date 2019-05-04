strr = ""
for x in range(2):
    for y in range(4):
        strr += "<siedzenie>\n<rzad>"+str(x+1)+"</rzad>\n<numer_siedzenia>"+str(y+1)+"</numer_siedzenia>\n</siedzenie>\n"
print(strr)