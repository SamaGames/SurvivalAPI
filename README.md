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
* **ParanoidModule** : Quand un joueur mine un minerai de diamant, ses coordonnées sont affichées dans le chat.
* **RandomChestModule** : Les coffres contiennent des contenus aléatoires prédéfinis.
* **RapidOresModule** : Permet de modifier l'objet donné par le cassage d'un minerai.
* **TorchThanCoalModule** : Le charbon ne peut plus s'obtenir à partir des minerais, ceux-ci donneront désormais des torches.
* **WorldDropModule** : Permet de modifier le retour de certains blocs du monde.

#### Combat

* **AutomaticTNTModule** : Active la TNT automatiquement quand elle est posée.
* **BombersModule** : Un briquet indestructible est donné aux joueurs au début du jeu, de plus, quand un joueur meurt, il donne de la TNT.
* **DropMyEffectsModule** : Quand un joueur est tué, celui-ci donne ses effets actuels sous forme de potion.
* **KillForEnchantmentModule** : Quand un joueur meurt, celui-ci donne une table d'enchantement. Le craft de celle-ci est bloqué par défaut.
* **KillToToggleTimeModule** : Quand un joueur meurt, l'heure du monde est inversée (jour / nuit).
* **OneShootPassiveModule** : Les entités passives meurt en un coup.
* **ThreeArrowModule** : Quand une flèche est tirée par un arc, celui-ci en tire trois au lieu d'une.

#### Craft

* **DisableFlintAndSteelModule** : Désactive le craft du briquet
* **DisableLevelTwoPotionModule** : Désactive le fait de pouvoir obtenir des potions de niveau II
* **DisableNotchAppleModule** : Désactive le craft de la pomme de Notch
* **DisableSpeckedMelonModule** : Désactive le craft du melon sintillant
* **OneWorkbenchModule** : Permet le craft d'une seule et unique table de craft. Le joueur devra la garder toute la partie.
* **RapidToolsModule** : Permet de modifier le matériel des outils quand un en bois est crafté.

#### Entités

* **EntityDropModule** : Permet de modifier ce que l'on peut obtenir des entités.
* **InfestationModule** : Quand un monstre est tué, il a 40% de chance de 'revivre' au même endroit.

#### Gameplay

* **AutomaticLapisModule** : Permet de remplir automatiquement le slot du Lapis Lazuli dans une table d'enchantement.
* **BloodDiamondModule** : Quand un minerai de diamant est cassé, le joueur perd de la vie.
* **CatsEyesModule** : Permet d'avoir constemment un effet de vision nocturne.
* **ConstantPotionModule** : Permet d'avoir des effets de potion constants.
* **DoubleHealthModule** : Permet de doubler la barre de vie originelle.
* **FastTreeModule** : Permet de casser instantanément les arbres.
* **LoveMachineModule** : EasterEgg.
* **NineSlotsModule** : Condamne l'inventaire du joueur aux neufs slots de la barre d'action rapide.
* **PersonalBlocksModule** : Restreint la destruction d'un bloc sensible à la personne qui l'a posé.
* **RapidFoodModule** : Modifie ce que les entité et blocs peuvent donner comme nourriture.
* **RapidStackingModule** : Permet d'obtenir le même type de bois et de pierre peu importe la catégorie.
* **RapidUsefullModule** : Permet de donner des objets définis sur certains blocs.
* **RemoveItemOnUseModule** : Supprime les bols de soupe quand celui-ci est consommé.
* **RottenPotionsModule** : Donne un effet de potion aléatoire quand un joueur mange de la nourriture de Zombie.
* **StackableItemModule** : Permet de fusionner les objets identiques dans le même stack.

### JavaDoc

[Link](http://aperture.samagames.net/javadoc-super-secrete-456FG45UJ/)

### Auteur

Jérémy L. (@BlueSlime)