from os import listdir
from os.path import isfile, join
from pandas import *
import email
import time
import re


## Helper Functions ##

def get_email_scrubber():
  email_regx = re.compile('[\w.%+-]+@[\w.-]+\.\w+')
  def scrub_email(str):
    m = email_regx.search(str)
    if m:
      return m.group(0)
    else:
      return str
  return scrub_email

scrub_email = get_email_scrubber()

def get_date_parser():
  date_regx = re.compile(r'\d+ \w+ \d{4} \d{2}:\d{2}:\d{2}')
  def parse_le_date(dstr):
    m = date_regx.search(dstr)
    if m:
      return time.strptime(m.group(0), "%d %b %Y %H:%M:%S")
    else:
      print "fail :" + dstr
      return time.strptime("2002", "%Y")
  return parse_le_date

parse_le_date = get_date_parser()


## SCRIPT

path = "03.data/easy_ham/"

email_files = [ join(path,f) for f in listdir(path) \
  if (isfile(join(path,f)) and f != 'cmds' and not(f.endswith('.swp'))) ]

email_attrs = {'Date': [], 'From': [], 'Subject': [], 'Message' : [], 'Path': [] }

for f in email_files:
  email_message = email.message_from_file(open(f))
  email_attrs['Date'].append(email_message['Date'])
  email_attrs['From'].append(email_message['From'])
  email_attrs['Subject'].append(email_message['Subject'])
  email_attrs['Message'].append(email_message.get_payload())
  email_attrs['Path'].append(f)

df = DataFrame(email_attrs)
df['Subject'] = [x.lower() for x in df['Subject'] if x]
df['From'] = [scrub_email(x.lower()) for x in df['From'] if x]
df['DateStructs'] = [parse_le_date(d) for d in df['Date']]

# message_column = df.pop('Message')
# subject_column = df.pop('Subject')
# path_column = df.pop('Path')

from_group = df.groupby('From')
from_group_size = from_group.size()
from_group_size.order(ascending=False)[:20].plot(kind='barh')

