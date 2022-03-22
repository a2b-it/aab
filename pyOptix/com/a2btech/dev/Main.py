'''
Created on 17 mar. 2022

@author: a.bouabidi
'''

from oauth2client.service_account import ServiceAccountCredentials
import gspread
import pandas as pd
from com.a2btech.dev.gdrive.oneSheetReader import gSheetReader
 
scope = ['https://spreadsheets.google.com/feeds',
         'https://www.googleapis.com/auth/drive'
]

def export_to_file (workbook):
    worksheet = workbook.worksheet('Menu')
    export_file = worksheet.export(format='xlsx')
    f = open('filename.xlsx', 'wb')
    f.write(export_file)
    f.close()
    


if __name__ == '__main__':
    GOOGLE_KEY_FILE = 'credentials.json'
    credentials = ServiceAccountCredentials.from_json_keyfile_name(GOOGLE_KEY_FILE, scope)
    gc = gspread.authorize(credentials)    
    wokbook_key = '1kkOb-tFKiree-i6vEq11bgUfkNlotnpc__UFGLT3YuE'
    workbook = gc.open_by_key(wokbook_key)
    reader = gSheetReader(workbook)
    reader.read_sheet_todb('Fournisseur')