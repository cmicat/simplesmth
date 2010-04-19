# -*- coding: utf-8 -*-
from django.conf.urls.defaults import *

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    # Example:
    # (r'^smth/', include('smth.foo.urls')),

    # Uncomment the admin/doc line below and add 'django.contrib.admindocs' 
    # to INSTALLED_APPS to enable admin documentation:
    # (r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # (r'^admin/', include(admin.site.urls)),
    (r'^login', 'smth.op.views.login'),
    (r'^logout', 'smth.op.views.logout'),
    (r'^getfav', 'smth.op.views.getfav'),
    (r'^gettop$', 'smth.op.views.gettop'),
    (r'^gettopic$', 'smth.op.views.gettopic'),
    (r'^getboard', 'smth.op.views.getboard'),
    (r'^article', 'smth.op.views.article'),
    (r'^post', 'smth.op.views.post'),
)
