# SurvivalAPI

La SurvivalAPI est une dépendance aux jeux basés sur la survite dite 'Hardcore' ; nous pouvons citer l'UHC, l'UHCRun, le SwitchRun ou encore le DoubleRunner.

Le principe est de centraliser en une seule et même API ce qui est commun à tous les jeux pour éviter une redondance de code.

Cette API est composée de 'modules'. C'est-à-dire que les jeux auront à activer les modules qu'ils souhaitent correspondants aux fonctionnalités qu'ils souhaitent. Seule la mécanique de survie Hardcore est activée par défaut et ne peut être désactivée. Grâce a cette API, la quantité de code des différents jeux cités dans le premier paragraphe a été extrèmement réduite. Tout cela a été réorganisé et centralisé dans la SurvivalAPI.

Il est libre à tous de proposer des idées de modules. Toute fois, l'usage de cette API est restreinte aux développeurs confirmés de l'équipe de développement, pour des raisons évidentes, les jeux qui utiliseront cette API seront sensibles.


### Modules

Les modules sont disposés dans des catégories différentes par question d'organisation.

#### Blocs

* **DiamondFlowerModule** : Active un poucentage de chance d'obtenir un diamant en cassant une fleur.
* **HardObsidianModule** : L'obsidienne endommage grandement votre pioche, de plus, seule une pioche en diamant peut la casser.
* **LightsOutModule** : Désactive la possibilité de poser des torches (tout type confondu).
* **PainfulStonesModule** : Donne des dégats quand le joueur marche sur certains types de blocs.
* **ParanoidModule** : Quand un joueur mine un minerai de diamant, ses coordonnées sont affichées dans le chat.
* **RandomChestModule** : Les coffres contiennent des contenus aléatoires prédéfinis.
* **RapidOresModule** : Permet de modifier l'objet donné par le cassage d'un minerai.
* **TorchThanCoalModule** : Le charbon ne peut plus s'obtenir à partir des minerais, ceux-ci donneront désormais des torches.
* **WorldDropModule** : Permet de modifier le retour de certains blocs du monde.

#### Combat

* **AutomaticTNTModule** : Active la TNT automatiquement quand elle est posée.
* **BombersModule** : Un briquet indestructible est donné aux joueurs au début du jeu, de plus, quand un joueur meurt, il donne de la TNT.
* **DropMyEffectsModule** : Quand un joueur est tué, celui-ci donne ses effets actuels sous forme de potion.
* **EveryRoseModule** : Donne un plastron enchanté Thorns à tous les joueurs au début de la partie.
* **KillForEnchantmentModule** : Quand un joueur meurt, celui-ci donne une table d'enchantement. Le craft de celle-ci est bloqué par défaut.
* **KillSwitchModule** : Quand un joueur meurt, son inventaire est échangé avec celui de son meurtrié.
* **KillToToggleTimeModule** : Quand un joueur meurt, l'heure du monde est inversée (jour / nuit).
* **MeleeFunModule** : Désactive le cooldown d'attaque entre les joueurs. Ils peuvent se taper très très rapidement.
* **OneShootPassiveModule** : Les entités passives meurt en un coup.
* **SpeedSwapModule** : Quand un joueur meurt, l'effet de vitesse devient effet de lenteur pour tous les joueurs. Le prochain joueur tué rééchangera ces effets.
* **StockupModule** : Quand un joueur meurt, des coeurs d'absorption sont donnés aux autres joueurs.
* **SwitcherooModule** : Quand une entité est touchée par la flèche d'un joueur, leurs positions sont échangées.
* **ThreeArrowModule** : Quand une flèche est tirée par un arc, celui-ci en tire trois au lieu d'une.

#### Craft

* **DisableFlintAndSteelModule** : Désactive le craft du briquet.
* **DisableLevelTwoPotionModule** : Désactive le fait de pouvoir obtenir des potions de niveau II.
* **DisableNotchAppleModule** : Désactive le craft de la pomme de Notch.
* **DisableSpeckedMelonModule** : Désactive le craft du melon scintillant.
* **InventorsModule** : Quand un joueur craft un outil ou morceau d'armure en diamant, une annonce est affichée dans le chat.
* **NoBowModule** : Désactive les arcs.
* **NoSwordModule** : Désactive les épées.
* **OneWorkbenchModule** : Permet le craft d'une seule et unique table de craft. Le joueur devra la garder toute la partie.
* **RapidToolsModule** : Permet de modifier le matériel des outils quand un en bois est crafté.

#### Entités

* **EntityDropModule** : Permet de modifier ce que l'on peut obtenir des entités.
* **InfestationModule** : Quand un monstre est tué, il a 40% de chance de 'revivre' au même endroit.
* **MobOresModule** : Quand un minerai est cassé, il y a 10% de chance de faire spawn un mob à cet endroit.
* **SpawnEggsModule** : Quand le joueur lance un oeuf, il fait spawner une entité aléatoire.
* **VengefulSpiritsModule** : Quand un joueur est tué, un Ghast ou un Blaze spawn.
* **ZombiesModule** : Quand un joueur est tué, un zombie avec ses effets spawn.

#### Gameplay

* **AutomaticLapisModule** : Permet de remplir automatiquement le slot du Lapis Lazuli dans une table d'enchantement.
* **BloodDiamondModule** : Quand un minerai de diamant est cassé, le joueur perd de la vie.
* **CatsEyesModule** : Permet d'avoir constemment un effet de vision nocturne.
* **ChickenModule** : Au début du jeu, tous les joueurs ont la vie d'un poulet (1.5 coeur) et des pommes en or.
* **CocoaEffectsModule** : Au début du jeu, tous les joueurs recoivent 5 cacao donnant 30 secondes d'effets positifs et ensuite 30 secondes d'effets négatifs.
* **ConstantPotionModule** : Permet d'avoir des effets de potion constants.
* **CookieHeadModule** : Quand un joueur meurt, sa tête possédant tous ses effets est droppée.
* **CrippleModule** : Quand un joueur prend des dégats de chute, un effet de lenteur est donné.
* **DoubleHealthModule** : Permet de doubler la barre de vie originelle.
* **FastTreeModule** : Permet de casser instantanément les arbres.
* **GapZapModule** : Quand un joueur prend des dégats, l'effet de régénération est annulé.
* **GoneFishingModule** : Au début du jeu, une canne à pêche incassable est donnée aux joueurs.
* **HighwayToHellModule** : Au début du jeu, des outils pour aller dans le Nether sont donnés aux joueurs.
* **InfiniteEnchanterModule** : Au début du jeu, des outils pour s'enchanter indéfiniment sont donnés au joueurs.
* **LoveMachineModule** : EasterEgg.
* **NightmareModule** : La partie se déroulera uniquement de nuit.
* **NineSlotsModule** : Condamne l'inventaire du joueur aux neufs slots de la barre d'action rapide.
* **NinjanautModule** : Au début de la partie, un joueur est désigné comme Ninjanaut. Des effets positifs lui seront donnés. A se mort, son meurtrié devient Ninjanaut.
* **OneHealModule** : Au début du jeu, une houe en or permettant de restaurer completement la vie est donnée aux joueurs.
* **PersonalBlocksModule** : Restreint la destruction d'un bloc sensible à la personne qui l'a posé.
* **PopeyeModule** : Au début de la partie, une épinard permettant d'avoir un effet de force est donnée aux joueurs.
* **PotentialHeartsModule** : Au début du jeu, les joueurs ont beaucoup de coeurs potentiels, mais seulement la moitiée est donnée.
* **PuppyPowerModule** : Au début du jeu, des oeufs de loup ainsi que du matériel pour apprivoiser sont donnés aux joueurs.
* **PyroTechnicsModule** : Quand un joueur prend des dégats, il est mit en feu. Cependant, un seau d'eau est donné au début de la partie à tous les joueurs.
* **RapidFoodModule** : Modifie ce que les entité et blocs peuvent donner comme nourriture.
* **RapidStackingModule** : Permet d'obtenir le même type de bois et de pierre peu importe la catégorie.
* **RapidUsefullModule** : Permet de donner des objets définis sur certains blocs.
* **RemoveItemOnUseModule** : Supprime les bols de soupe quand celui-ci est consommé.
* **RiskyRetrievalModule** : Quand un minerai est miné, il est instantanément copié dans un coffre incassable situé au centre de la carte.
* **RottenPotionsModule** : Donne un effet de potion aléatoire quand un joueur mange de la nourriture de Zombie.
* **StackableItemModule** : Permet de fusionner les objets identiques dans le même stack.
* **SuperheroesModule** : Tous les joueurs obtiennent des effets de résistance accrue.
* **SuperheroesModulePlus** : Tous les joueurs obtiennent des effets de résistance très importants.
* **TheHobbitModule** : Au début du jeu, un anneau d'or permettant de devenir invisible est donné aux joueurs.

### JavaDoc

[Link](http://blackmesa.samagames.net/javadoc-super-secrete-456FG45UJ/)

### Auteur

Jérémy L. (@BlueSlime)