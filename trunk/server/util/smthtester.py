#!/usr/bin/env python
# -*- coding: utf-8 -*-


import smthapi
import smthparser

smth = smthapi.SimpleSMTH()
out=smth.get_url_data('http://www.newsmth.net/bbsdoc.php?board=Aero&ftype=6&page=246')
print unicode(out,"gbk")
processor = smthparser.TopicProcessor(unicode(out,"gbk"))
print processor.getall()
#print 'posting'
#out=smth.post_url_data('http://www.newsmth.net/atomic.php?act=post&board=Test&reid=0&post=1',{'text': u'\u6d4b\u8bd5'.encode#('gbk'), 'title': u'\u6d4b\u8bd5'.encode('gbk')})
#print unicode(out,"gbk")
#out=smth.get_url_data('http://www.newsmth.net/rssi.php?h=1')
#print out
#parser = smthparser.Top10Parser(out)
#print parser.getall()