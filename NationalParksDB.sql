DROP TABLE IF EXISTS NationalParks, UserBlog, PublicBlog, UserFile, UserPhoto, UserTrip, User;

CREATE TABLE NationalParks
(
	id INT UNSIGNED NOT NULL AUTO_INCREMENT
	,fullName VARCHAR(44) NOT NULL
        ,twitterHandle VARCHAR(15) NOT NULL
        ,states VARCHAR(8) NOT NULL
	,parkCode VARCHAR(4) NOT NULL
	,imageUrl VARCHAR(90) NOT NULL
	,PRIMARY KEY (id)
);

INSERT INTO NationalParks (fullName, twitterHandle, parkCode, states, imageUrl) VALUES
('Acadia National Park','AcadiaNPS','acad','ME','https://www.nps.gov/common/uploads/structured_data/3C7B45AE-1DD8-B71B-0B7EE131C7DFC2F5.jpg')
,('Arches National Park','ArchesNPS','arch','UT','https://www.nps.gov/common/uploads/structured_data/3C79850F-1DD8-B71B-0BC4A88BA85DE6B0.jpg')
,('Badlands National Park','BadlandsNPS','badl','SD','https://upload.wikimedia.org/wikipedia/commons/b/b9/MK00609_Badlands.jpg')
,('Big Bend National Park','BigBendNPS','bibe','TX','https://www.nps.gov/common/uploads/structured_data/3C84EF64-1DD8-B71B-0B44D9F693CAA78C.jpg')
,('Biscayne National Park','BiscayneNPS','bisc','FL','https://www.nps.gov/common/uploads/structured_data/3C870533-1DD8-B71B-0B70CFF5EF6538F1.jpg')
,('Black Canyon Of The Gunnison National Park','BlackCanyonNPS','blca','CO','https://www.nps.gov/common/uploads/structured_data/3C81655F-1DD8-B71B-0B4BCFFDB74EE723.jpg')
,('Bryce Canyon National Park','BryceCanyonNPS','brca','UT','https://www.nps.gov/common/uploads/structured_data/3C7F8B29-1DD8-B71B-0B5EA38E8C5E5606.jpg')
,('Canyonlands National Park','CanyonlandsNPS','cany','UT','https://www.nps.gov/common/uploads/structured_data/3C7A4FC2-1DD8-B71B-0B13118C99270C08.jpg')
,('Capitol Reef National Park','CapitolReefNPS','care','UT','https://www.nps.gov/common/uploads/structured_data/3C82E3C7-1DD8-B71B-0B4181834EE46AED.jpg')
,('Carlsbad Caverns National Park','CavernsNPS','cave','NM','https://www.nps.gov/common/uploads/structured_data/3C82342F-1DD8-B71B-0BAD8438A2A16379.jpg')
,('Channel Islands National Park','CHISNPS','chis','CA','https://www.nps.gov/common/uploads/structured_data/3C7A6DDF-1DD8-B71B-0B7621DF7FCB2093.jpg')
,('Congaree National Park','CongareeNPS','cong','SC','https://www.nps.gov/common/uploads/structured_data/3C862C60-1DD8-B71B-0BB65F7B652BA840.jpg')
,('Crater Lake National Park','CraterLakeNPS','crla','OR','https://www.nps.gov/common/uploads/structured_data/3C7B227E-1DD8-B71B-0BEECDD24771C381.jpg')
,('Cuyahoga Valley National Park','CVNPNPS','cuva','OH','https://www.nps.gov/common/uploads/structured_data/50001FF6-1DD8-B71B-0BECA954B0F991BF.jpg')
,('Death Valley National Park','DeathValleyNPS','deva','CA,NV','https://www.nps.gov/common/uploads/structured_data/3C7EC929-1DD8-B71B-0B6F8851F7D62846.jpg')
,('Denali National Park & Preserve','DenaliNPS','dena','AK','https://www.nps.gov/common/uploads/structured_data/3C83C9C7-1DD8-B71B-0B9B669ED961F97E.jpg')
,('Dry Tortugas National Park','DryTortugasNPS','drto','FL','https://www.nps.gov/common/uploads/structured_data/3C80FF02-1DD8-B71B-0B39AC51BF7B2FA2.jpg')
,('Everglades National Park','EvergladesNPS','ever','FL','https://www.nps.gov/common/uploads/structured_data/3C854681-1DD8-B71B-0BA4F6D9379336DF.jpg')
,('Gates Of The Arctic National Park & Preserve','GatesArcticNPS','gaar','AK','https://www.nps.gov/common/uploads/structured_data/3C7A89F4-1DD8-B71B-0B52204A2EBF61A4.jpg')
,('Gateway Arch National Park','GatewayArchNPS','jeff','MO','https://www.nps.gov/common/uploads/structured_data/3C7BD9B5-1DD8-B71B-0B598216CE4E46D0.jpg')
,('Glacier Bay National Park & Preserve','GlacierBayNPS','glba','AK','https://www.nps.gov/common/uploads/structured_data/3C790BBF-1DD8-B71B-0B0AE92D0B9C24EB.jpg')
,('Glacier National Park','GlacierNPS','glac','MT','https://www.nps.gov/common/uploads/structured_data/3C7FEF84-1DD8-B71B-0B26F3C536955733.jpg')
,('Grand Canyon National Park','GrandCanyonNPS','grca','AZ','https://www.nps.gov/common/uploads/structured_data/3C7B12D1-1DD8-B71B-0BCE0712F9CEA155.jpg')
,('Grand Teton National Park','GrandTetonNPS','grte','WY','https://www.nps.gov/common/uploads/structured_data/3C7FA4C5-1DD8-B71B-0B7FCC54E43FEE79.jpg')
,('Great Basin National Park','GreatBasinNPS','grba','NV','https://www.nps.gov/common/uploads/structured_data/3C876E30-1DD8-B71B-0B6A6CDF68B4FA89.jpg')
,('Great Sand Dunes National Park & Preserve','NatlParkService','grsa','CO','https://www.nps.gov/common/uploads/structured_data/3C7CE386-1DD8-B71B-0B14D302825B96CF.jpg')
,('Great Smoky Mountains National Park','GreatSmokyNPS','grsm','NC,TN','https://www.nps.gov/common/uploads/structured_data/3C80E3F4-1DD8-B71B-0BFF4F2280EF1B52.jpg')
,('Guadalupe Mountains National Park','GuadalupeMtnsNP','gumo','TX','https://www.nps.gov/common/uploads/structured_data/3C825533-1DD8-B71B-0B6FDF436F604A3C.jpg')
,('Haleakalá National Park','HaleakalaNPS','hale','HI','https://www.nps.gov/common/uploads/structured_data/3C87A368-1DD8-B71B-0BD44B189D0D9368.jpg')
,("Hawai'i Volcanoes National Park",'Volcanoes_NPS','havo','HI','https://www.nps.gov/common/uploads/structured_data/3C7AE1CB-1DD8-B71B-0B485C48F98A0041.jpg')
,('Hot Springs National Park','NatlParkService','hosp','AR','https://www.nps.gov/common/uploads/structured_data/3C8309AE-1DD8-B71B-0B640467D9BE54A5.jpg')
,('Isle Royale National Park','NatlParkService','isro','MI','https://www.nps.gov/common/uploads/structured_data/3C7AC355-1DD8-B71B-0B9C2F07853F39F1.jpg')
,('Joshua Tree National Park','JoshuaTreeNPS','jotr','CA','https://www.nps.gov/common/uploads/structured_data/3C85E84D-1DD8-B71B-0B188E7820D60F14.jpg')
,('Katmai National Park & Preserve','KatmaiNPS','katm','AK','https://www.nps.gov/common/uploads/structured_data/3C7A0FDB-1DD8-B71B-0B8933ACA92FE6B3.jpg')
,('Kenai Fjords National Park','KenaiFjordsNPS','kefj','AK','https://www.nps.gov/common/uploads/structured_data/3C798EAB-1DD8-B71B-0BC4BEFB197F2C90.jpg')
,('Kobuk Valley National Park','NatlParkService','kova','AK','https://www.nps.gov/common/uploads/structured_data/3C7A1214-1DD8-B71B-0B00D823BD9BF4CF.jpg')
,('Lake Clark National Park & Preserve','LakeClarkNPS','lacl','AK','https://www.nps.gov/common/uploads/structured_data/3C7A925C-1DD8-B71B-0B35931EE9B97899.jpg')
,('Lassen Volcanic National Park','LassenNPS','lavo','CA','https://www.nps.gov/common/uploads/structured_data/3C873811-1DD8-B71B-0B9C62ED8E12E7B5.jpg')
,('Mammoth Cave National Park','MammothCaveNP','maca','KY','https://www.nps.gov/common/uploads/structured_data/6FE16EEF-1DD8-B71B-0BA9538F9BF50B2F.jpg')
,('Mesa Verde National Park','NatlParkService','meve','CO','https://www.nps.gov/common/uploads/structured_data/3C7C0089-1DD8-B71B-0BC397BA671C0616.jpg')
,('Mount Rainier National Park','MountRainierNPS','mora','WA','https://www.nps.gov/common/uploads/structured_data/3C7C68E6-1DD8-B71B-0B42E9A3C7ECA52D.jpg')
,('National Park of American Samoa','NPamericansamoa','npsa','AS','https://www.nps.gov/common/uploads/structured_data/3C84F643-1DD8-B71B-0BC6F3EA2E1F58AB.jpg')
,('North Cascades National Park','NCascadesNPS','noca','WA','https://www.nps.gov/common/uploads/structured_data/3C7A599D-1DD8-B71B-0BBDC12BEC5107B5.jpg')
,('Olympic National Park','OlympicNP','olym','WA','https://www.nps.gov/common/uploads/structured_data/3C7B1A8C-1DD8-B71B-0B15D930BD8214F5.jpg')
,('Petrified Forest National Park','PetrifiedNPS','pefo','AZ','https://www.nps.gov/common/uploads/structured_data/3C7D8213-1DD8-B71B-0BE4A3B9394FD89A.jpg')
,('Pinnacles National Park','PinnaclesNPS','pinn','CA','https://www.nps.gov/common/uploads/structured_data/3C86A8CB-1DD8-B71B-0BAE8F7141CCBB1B.jpg')
,('Redwood National and State Parks','RedwoodNPS','redw','CA','https://www.nps.gov/common/uploads/structured_data/13B07C75-1DD8-B71B-0BD552B11A6CA62A.jpg')
,('Rocky Mountain National Park','RockyNPS','romo','CO','https://www.nps.gov/common/uploads/structured_data/3C7ECB23-1DD8-B71B-0BC28A0330D6D8D6.png')
,('Saguaro National Park','SaguaroNPS','sagu','AZ','https://www.nps.gov/common/uploads/structured_data/3C858462-1DD8-B71B-0BB499810C61332C.jpg')
,('Sequoia & Kings Canyon National Parks','SequoiaKingsNPS','seki','CA','https://www.nps.gov/common/uploads/structured_data/3C7A250B-1DD8-B71B-0BCF61A89A8B2970.jpg')
,('Shenandoah National Park','ShenandoahNPS','shen','VA','https://www.nps.gov/common/uploads/structured_data/3C80B539-1DD8-B71B-0BEAAA4AC31E7D5B.jpg')
,('Theodore Roosevelt National Park','TRooseveltNPS','thro','ND','https://www.nps.gov/common/uploads/structured_data/3C7939C8-1DD8-B71B-0B048D7EC3812DE3.jpg')
,('Virgin Islands National Park','NatlParkService','viis','VI','https://www.nps.gov/common/uploads/structured_data/3C79CCA2-1DD8-B71B-0BB6BCFC19DE82D1.jpg')
,('Voyageurs National Park','NatlParkService','voya','MN','https://www.nps.gov/common/uploads/structured_data/3C8405EF-1DD8-B71B-0B42909E4E77E144.jpg')
,('Wind Cave National Park','WindCaveNPS','wica','SD','https://www.nps.gov/common/uploads/structured_data/3C7ACD12-1DD8-B71B-0BEF13804E4498FF.jpg')
,('Wrangell - St Elias National Park & Preserve','WrangellStENPS','wrst','AK','https://www.nps.gov/common/uploads/structured_data/3C7AAC04-1DD8-B71B-0B6534785C41D6B5.jpg')
,('Yellowstone National Park','YellowstoneNPS','yell','ID,MT,WY','https://www.nps.gov/common/uploads/structured_data/3C7D2FBB-1DD8-B71B-0BED99731011CFCE.jpg')
,('Yosemite National Park','YosemiteNPS','yose','CA','https://www.nps.gov/common/uploads/structured_data/3C84C3C0-1DD8-B71B-0BFF90B64283C3D8.jpg')
,('Zion National Park','ZionNPS','zion','UT','https://www.nps.gov/common/uploads/structured_data/3C7EFF41-1DD8-B71B-0B50E940FE9F2658.jpg');

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(256) NOT NULL,          /* To store Salted and Hashed Password Parts */
    first_name VARCHAR(32) NOT NULL,
    middle_name VARCHAR(32),
    last_name VARCHAR(32) NOT NULL,
    address1 VARCHAR(128) NOT NULL,
    address2 VARCHAR(128),
    city VARCHAR(64) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,            /* e.g., 24060-1804 */
    security_question_number INT NOT NULL,   /* Refers to the number of the selected security question */
    security_answer VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,      
    PRIMARY KEY (id)
);

CREATE TABLE UserBlog
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    park VARCHAR(44) NOT NULL,
    description VARCHAR(8000) NOT NULL,
    rating VARCHAR(7) NOT NULL,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE PublicBlog
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    park VARCHAR(44) NOT NULL,
    description VARCHAR(8000) NOT NULL,
    rating VARCHAR(7) NOT NULL,
    PRIMARY KEY (id)
);

/* The UserPhoto table contains attributes of interest of a user's photo. */
CREATE TABLE UserPhoto
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

/* The UserFile table contains attributes of interest of a user's uploaded file. */
CREATE TABLE UserFile
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       filename VARCHAR(256) NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE UserTrip
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date_entered DATE NOT NULL,
    trip MEDIUMTEXT NOT NULL,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);