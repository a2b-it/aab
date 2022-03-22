'''
Created on 17 mar. 2022

@author: a.bouabidi
'''

import gspread
import pandas as pd



class gSheetReader(object):
    '''
    classdocs
    '''
    workbook:any

    def __init__(self, workbook):
        '''
        Constructor
        '''
        self.workbook=workbook;
    
    
    def read_sheet_todb(self, name:str) :
        worksheet = self.workbook.worksheet(name)
        #listvalue = worksheet.get_all_values()
        list_of_dicts = worksheet.get_all_records()
        print(list_of_dicts)