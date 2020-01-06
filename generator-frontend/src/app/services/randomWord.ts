const animals =  ['aardvark','albatross','alligator','alpaca','angelfish','anteater','antelope','armadillo','badger','barracuda','bat','beagle','bear','beaver','bird','brontosaurus',
    'boaconstrictor','bulldog','bumblebee','butterfly','camel','caribou','cassowary','cat','catfish','caterpillar','centipede','chameleon','cheetah','chinchilla','chipmunk'
    ,'cobra','coelacanth','condor','coralsnake','cormorant','crab','crane','crocodile','dalmatian','deer','dolphin','dove','dragonfish','dragonfly','duck','eagle','eel','elephant',
    'elk','falcon','ferret','finch','firefly','fish','flamingo','fox','frog','gazelle','gecko','gerbil','giraffe','gnu','goldfish','goose','gorilla','grasshopper','greyhound','grouse',
    'gull','hamster','hare','hawk','hatchetfish','hedgehog','heron','herring','hornet','horse','hummingbird','ibex','ibis','iguana','jackal','jaguar','jellyfish','kangaroo','kestrel',
    'kingfisher','koala','koi','lark','lemur','leopard','lion','lionfish','llama','lobster','loris','magpie','mallard','mandrill','mantaray','mantis','marlin','mastiff','mollusk',
    'mongoose','moose','mouse','narwhal','nautilus','newt','nightingale','octopus','okapi','opossum','orca','osprey','ostrich','otter','owl','panda','panther','parrot','partridge',
    'pelican','penguin','pheasant','pigeon','platypus','polarbear','porcupine','porpoise','python','quail','rabbit','raccoon','ram','raven','reindeer','rhinoceros','roadrunner','rook',
    'salamander','salmon','sandpiper','scorpion','seacucumber','sealion','seasnake','seaturtle','seahorse','seal','shark','sheep','snowyowl','songbird','sparrow','spider','squid',
    'squirrel','starfish','starling','stegosaurus','stingray','stork','swan','tapir','tiger','toucan','triceratops','turtle','vampirebat','velociraptor','wallaby','walrus','wolf',
    'wolverine','wombat','wrasse','wren','yak','zebra'];

const nouns = ['apple','baby','back','ball','bear','bed','bell','bird','birthday','boat','box','boy','bread','brother','cake','car','cat','chair','chicken','children','coat','corn',
    'cow','day','dog','doll','door','duck','egg','eye','farm','farmer','father','feet','fire','fish','floor','flower','game','garden','girl','grass','ground','hand','head','hill','home',
    'horse','house','kitty','leg','letter','man','men','milk','money','morning','mother','name','nest','night','paper','party','picture','pig','rabbit','rain','ring','robin','school',
    'seed','sheep','shoe','sister','snow','song','squirrel','stick','street','sun','table','thing','time','top','toy','tree','watch','water','way','wind','window','wood'];

const adjectives = ['abrupt','acidic','adorable','adventurous','aggressive','agitated','alert','aloof','amiable','amused','annoyed','antsy','anxious','appalling','appetizing',
    'apprehensive','arrogant','ashamed','astonishing','attractive','average','batty','beefy','bewildered','biting','bitter','bland','blushing','bored','brave','bright','broad','bulky',
    'burly','charming','cheeky','cheerful','chubby','clean','clear','cloudy','clueless','clumsy','colorful','colossal','combative','comfortable','condemned','condescending','confused',
    'contemplative','convincing','convoluted','cooperative','corny','costly','courageous','crabby','creepy','crooked','cruel','cumbersome','curved','cynical','dangerous','dashing',
    'decayed','deceitful','deep','defeated','defiant','delicious','delightful','depraved','depressed','despicable','determined','dilapidated','diminutive','disgusted','distinct',
    'distraught','distressed','disturbed','dizzy','drab','drained','dull','eager','ecstatic','elated','elegant','emaciated','embarrassed','enchanting','encouraging','energetic',
    'enormous','enthusiastic','envious','exasperated','excited','exhilarated','extensive','exuberant','fancy','fantastic','fierce','filthy','flat','floppy','fluttering','foolish',
    'frantic','fresh','friendly','frightened','frothy','frustrating','funny','fuzzy','gaudy','gentle','ghastly','giddy','gigantic','glamorous','gleaming','glorious','gorgeous',
    'graceful','greasy','grieving','gritty','grotesque','grubby','grumpy','handsome','happy','harebrained','healthy','helpful','helpless','high','hollow','homely','horrific','huge',
    'hungry','hurt','icy','ideal','immense','impressionable','intrigued','irate','irritable','itchy','jealous','jittery','jolly','joyous','filthy','flat','floppy','fluttering','foolish',
    'frantic','fresh','friendly','frightened','frothy','frustrating','funny','fuzzy','gaudy','gentle','ghastly','giddy','gigantic','glamorous','gleaming','glorious','gorgeous',
    'graceful','greasy','grieving','gritty','grotesque','grubby','grumpy','handsome','happy','harebrained','healthy','helpful','helpless','high','hollow','homely','horrific','huge',
    'hungry','hurt','icy','ideal','immense','impressionable','intrigued','irate','irritable','itchy','jealous','jittery','jolly','joyous','juicy','jumpy','kind','lackadaisical','large',
    'lazy','lethal','little','lively','livid','lonely','loose','lovely','lucky','ludicrous','macho','magnificent','mammoth','maniacal','massive','melancholy','melted','miniature',
    'minute','mistaken','misty','moody','mortified','motionless','muddy','mysterious','narrow','nasty','naughty','nervous','nonchalant','nonsensical','nutritious','nutty','obedient',
    'oblivious','obnoxious','odd','old-fashioned','outrageous','panicky','perfect','perplexed','petite','petty','plain','pleasant','poised','pompous','precious','prickly','proud',
    'pungent','puny','quaint','quizzical','ratty','reassured','relieved','repulsive','responsive','ripe','robust','rotten','rotund','rough','round','salty','sarcastic','scant','scary',
    'scattered','scrawny','selfish','shaggy','shaky','shallow','sharp','shiny','short','silky','silly','skinny','slimy','slippery','small','smarmy','smiling','smoggy','smooth','smug',
    'soggy','solid','sore','sour','sparkling','spicy','splendid','spotless','square','stale','steady','steep','responsive','sticky','stormy','stout','straight','strange','strong',
    'stunning','substantial','successful','succulent','superficial','superior','swanky','sweet','tart','tasty','teeny','tender','tense','terrible','testy','thankful','thick',
    'thoughtful','thoughtless','tight','timely','tricky','trite','troubled','uneven','unsightly','upset','uptight','vast','vexed','victorious','virtuous','vivacious','vivid','wacky',
    'weary','whimsical','whopping','wicked','witty','wobbly','wonderful','worried','yummy','zany','zealous','zippy'];

const polite_adjectives = ['adorable','acrobatic','adaptable','adventurous','agile','alert','amiable','amused','arboreal','ardent','artful','astonishing','astute','attentive',
    'authentic','avid','beardless','benevolent','bionic','blissful','bodacious','brave','bright','brilliant','bubbly','careful','cautious','charming','cheeky','cheerful','circumspect',
    'cognizant','collectible','colorful','colossal','comfortable','communicative','compact','compassionate','constant','contemplative','convincing','convivial','cooperative',
    'courageous','cordial','cosmic','creative','cryptic','crystalline','cunning','curious','dancing','daring','dauntless','dashing','dazzling','defiant','delightful','determined',
    'dexterous','diminutive','discerning','distinctive','dreaming','dynamic','eager','earnest','easygoing','eccentric','ecstatic','effervescent','elaborate','elated','elegant',
    'eloquent','elusive','enchanting','encouraging','energetic','energized','enormous','enthusiastic','erudite','essential','ethereal','excited','exhilarated','extraordinary',
    'exotic','exuberant','fantastic','fearless','feisty','fierce','fiery','flourishing','flying','focused','fortunate','friendly','frolicking','gargantuan','gesticulating','gigantic',
    'glamorous','gleaming','gleeful','glorious','gorgeous','graceful','grateful','gregarious','happy','harmonious','hatless','healthy','helpful','heroic','hydraulic','idealistic',
    'illustrious','imaginative','immense','impartial','imperturbable','improbable','incredible','inimitable','influential','inquisitive','insightful','inspired','intrepid','intricate',
    'intuitive','invaluable','inventive','jaunty','jolly','joyful','joyous','jubilant','jumping','keen','laughing','legendary','lenient','lively','loquacious','lucky','luminescent',
    'magnetic','magnificent','majestic','marvelous','masked','massive','mechanical','mercurial','meritorious','merry','methodical','meticulous','mighty','miniature','mirthful',
    'mischievous','modest','momentous','motionless','multicolored','murmuring','musical','mustachioed','mysterious','nascent','neighborly','noble','nomadic','nonchalant',
    'noncommittal','observant','omnidirectional','omnivorous','optimal','optimistic','otherworldly','outgoing','outspoken','panoramic','peaceful','perceptive','perpetual','perplexing',
    'perspicacious','philosophical','picturesque','playful','pleasant','poised','practical','precious','precise','precocious','prestigious','primeval','primordial','prismatic',
    'proactive','proficient','prototypical','prudent','purposeful','qualified','quizzical','quotable','radiant','reassured','reclusive','recurring','reflective','rejoicing','relaxed',
    'relieved','remarkable','renowned','resilient','resolute','resourceful','rigorous','roaring','robust','salient','salubrious','sanguine','sapient','sarcastic','satisfied','scholarly',
    'scintillating','scrupulous','selective','shiny','sincere','singing','sleek','sleepy','slippery','smiling','smooth','solid','sophisticated','sparkling','spectacular','splendid',
    'spotless','squeaky','stately','steady','strategic','striped','stunning','stupendous','stylish','substantial','sufficient','swimming','symbolic','symmetrical','taciturn',
    'terrestrial','tessellated','thankful','theoretical','thoughtful','thriving','timeless','timely','topographical','transparent','tranquil','transparent','tricky','ubiquitous',
    'uncanny','unclouded','undisputed','unexpected','unfathomable','unflappable','unique','universal','unofficial','unseen','unthinkable','uproarious','variegated','versatile',
    'victorious','vigilant','vigorous','virtuous','vivacious','vivid','vociferous','wacky','wandering','watchful','whimsical','windswept','witty','wonderful','wondrous','zany',
    'zealous','zestful'];

/** Creates a random word combination which should be almost unique */
export class RandomWord {

    /**
     * Generates a random word combination consisting of adjective and noun
     * 
     * @returns A random word combination consisting of adjective and noun
     */
    static getRandomWordCombination(): string {
        let result = "";

        if (Math.random() > 0.5) {
            result += adjectives[Math.floor(Math.random() * adjectives.length)] + "-";
        } else {
            result += polite_adjectives[Math.floor(Math.random() * polite_adjectives.length)] + "-";
        }

        if (Math.random() > 0.5) {
            result += animals[Math.floor(Math.random() * animals.length)];
        } else {
            result += nouns[Math.floor(Math.random() * nouns.length)];
        }

        return result;
    }
}