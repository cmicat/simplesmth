#!/usr/bin/env python


import oldsmth
import basesmth

smth = oldsmth.SimpleSMTH()
r=smth.smthLogin('datastream','790711')
print "logined:r"+str(r)
#out=smth.get_url_data('http://www.newsmth.net/atomic.php')
#print unicode(out,"gbk")
#print 'posting'
#out=smth.post_url_data('http://www.newsmth.net/atomic.php?act=post&board=Test&reid=0&post=1',{'text': u'\u6d4b\u8bd5'.encode#('gbk'), 'title': u'\u6d4b\u8bd5'.encode('gbk')})
#print unicode(out,"gbk")
