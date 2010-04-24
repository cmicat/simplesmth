# # -*- coding: utf-8 -*-  
from django.http import HttpResponse
import smth.util.mcconns as context
from smth.util.smthapi import SimpleSMTH
from smth.util.smthparser import FavProcessor
from smth.util.smthparser import BoardProcessor
from smth.util.smthparser import ArticleProcessor
from smth.util.smthparser import BeautyArticleProcessor
from smth.util.smthparser import Top10Parser
from smth.util.smthparser import TopicProcessor
from smth.util.json import json_encode
from smth.util.json import json_result
from django.conf import settings
import logging
import os

smthurl=settings.SMTH_URL

def login(request):
    userid=request.GET['id']
    passwd=request.GET['passwd']
    smth2=SimpleSMTH()
    r=smth2.smthLogin(userid,passwd)
    logging.info('login user:'+userid+',r:'+str(r))
    if r == 0:
        key=context.gen_sid()
        smth2.saveCj(key)
    else:
        key='0' #login fail
    result = {}
    result['k'] = key
    return HttpResponse(json_encode(result))

def logout(request):
    try:
        key=request.GET['key']
    except:
        logging.error('login key not found ')
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))    
    result={}
    result['l']=1
    try:
        context.remove_ck(key)
        result['r'] = 0
    except:
        logging.warn('logout a invalid key')
        result['r'] = 1
    logging.info('logout key:'+key)
    result2 = json_encode(result)
    return HttpResponse(result2)
            
    
def getfav(request):
    try:
        key=request.GET['key']
#        cjpath = context.getConnection(key)
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        logging.error('login key not found ')
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    simpleout=smth2.get_url_data(smthurl)
    print simpleout
    process = FavProcessor()
    process.feed(simpleout)
    result['r']=process.getfav()
    result2 = json_encode(result)
    process.close
    logging.info('getfav,r:'+result2)
    print result2
    return HttpResponse(result2)
    
    
def getboard(request):
    try:
        key=request.GET['key']
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    name = request.GET['board']
    try:
        page = request.GET['page']
    except:
        page = 0
    try:
        ftype = request.GET['ftype']
    except:
        ftype = 0
    if page > 0:
        simpleout=smth2.get_url_data(smthurl+'?act=board&board='+name+'&page='+page+'&ftype='+str(ftype))
    else:
        simpleout=smth2.get_url_data(smthurl+'?act=board&board='+name+'&ftype='+str(ftype))
    process = BoardProcessor()
    process.feed(unicode(simpleout,"gbk",'ignore'))
#    process.feed(simpleout)
    result['r']=process.showboard()
    result['pp']=process.getppage()
    result['np']=process.getnpage()
    process.close
    result2 = json_encode(result)
    print result2
    return HttpResponse(result2)

def gettopic(request):
    try:
        key=request.GET['key']
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    name = request.GET['board']
    try:
        page = request.GET['page']
    except:
        page = 0
    try:
        ftype = request.GET['ftype']
    except:
        ftype = 6
    if page > 0:
        simpleout=smth2.get_url_data('http://www.newsmth.net/bbsdoc.php?&board='+name+'&page='+page+'&ftype='+str(ftype))
    else:
        simpleout=smth2.get_url_data('http://www.newsmth.net/bbsdoc.php?board='+name+'&ftype='+str(ftype))
    process = TopicProcessor(unicode(simpleout,"gbk",'ignore'))
    result['r']=process.getall()
    result['p']=process.getpage()
    result2 = json_encode(result)
    print result2
    return HttpResponse(result2)


     
def article(request):
    try:
        key=request.GET['key']
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    articleId = request.GET['id']
    board = request.GET['board']
    try:
        p = request.GET['p']
    except:
        p = 0
    if p!=0:
        simpleout=smth2.get_url_data(smthurl+'?act=article&board='+board+'&id='+articleId+'&p='+p)
    else:
        simpleout=smth2.get_url_data(smthurl+'?act=article&board='+board+'&id='+articleId)
    process = ArticleProcessor()
    process.feed(unicode(simpleout,"gbk",'ignore'))
#    process.feed(simpleout)
    content = process.show()
    result['r']=content
    result['t']=process.gettitle()
    result['tid'] = process.gettopid()
    result['id']=process.getid()
    process.close
    result2 = json_encode(result)
    print result2
    return HttpResponse(result2)

def beautya(request): #beautiful article new version only
    try:
        key=request.GET['key']
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    articleId = request.GET['id']
    board = request.GET['board']
    try:
        p = request.GET['p']
    except:
        p = 0
    if p!=0:
        simpleout=smth2.get_url_data(smthurl+'?act=article&board='+board+'&id='+articleId+'&p='+p)
    else:
        simpleout=smth2.get_url_data(smthurl+'?act=article&board='+board+'&id='+articleId)
    process = BeautyArticleProcessor()
    process.feed(unicode(simpleout,"gbk",'ignore'))
#    process.feed(simpleout)
    content = process.getall()
    result['r']=content
    process.close
    result2 = json_encode(result)
    print result2
    return HttpResponse(result2)


def post(request):
    try:
        key=request.GET['key']
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    board= request.GET['board']
    title = request.POST['title']
    content = request.POST['content']
    reid = request.GET['reid']
    data = {'title':title.encode('gbk'),'text':content.encode('gbk')}
    print data
    simpleout=smth2.post_url_data(smthurl+'?act=post&board='+board+'&reid='+reid+'&post=1',data)
    if len(simpleout) > 1:
        result['r'] = 0
    else:
        result['r'] = 1
    result2 = json_encode(result)
    print result2
    return HttpResponse(result2)

topfeed=settings.SMTH_TOPFEED

def gettop(request):
    try:
        key=request.GET['key']
        smth2 = SimpleSMTH()
        smth2.setCj(key)
    except:
        fail = {}
        fail['l']=0
        return HttpResponse(json_encode(fail))
    result={}
    result['l']=1
    simpleout=smth2.get_url_data(topfeed)
    parser = Top10Parser(simpleout)
    content = parser.getall()
    result['r'] = content
    result2 = json_encode(result)
    return HttpResponse(result2)
