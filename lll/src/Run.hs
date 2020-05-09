{-# LANGUAGE NoImplicitPrelude #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes       #-}
{-# LANGUAGE TemplateHaskell   #-}
module Run (run) where

import           Control.Lens.TH
import           Control.Lens.Tuple
import           Control.Lens
import           Data.Aeson         (Value)
import           Data.Aeson.Lens
import           Data.Aeson.QQ
import           RIO hiding (view, (^.))
import           Types


type Degrees = Double
type Latitude = Degrees
type Longitude = Degrees

data Meetup = Meetup { _name  :: String, _location :: (Latitude, Longitude)}
makeLenses ''Meetup

newBestBud :: Value
newBestBud = [aesonQQ|
  {
    name: "James",
    location: {city: "New Orleans"}
  }
|]

oldBestBud :: Value
oldBestBud = [aesonQQ|
  {
    name: "Rachel",
    location: {city: "New York"}
  }
|]

john :: Value
john = [aesonQQ|
  {
    age: 23,
    name: "John",
    bestBud: {name: "Rachel", location: {city: "New York"}},
    likes: ["linux", "Haskell"]
  }
|]

myMeetup :: Meetup
myMeetup = Meetup "Amazing" (1.2, 2.1)

run :: RIO App ()
run = do
  logInfo . display $ (view _1 ("goal", "chaff") :: Text)
  logInfo . display $ view (location . _1) myMeetup
  logInfo . display $ john ^. key "name" . _String
  logInfo . display $
    john
      ^. key "bestBud" . _Value
      . key "location" . _Value
      . key "city" . _String
  logInfo . display . tshow $ john ^.. key "likes" . values . _String
  logInfo "We're inside the application!"

