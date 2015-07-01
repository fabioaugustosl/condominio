/*
SQLyog - Free MySQL GUI v5.19
Host - 5.1.36-community-log : Database - db_condominio
*********************************************************************
Server version : 5.1.36-community-log
*/

SET NAMES utf8;

SET SQL_MODE='';

create database if not exists `db_condominio`;

USE `db_condominio`;

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';

/*Table structure for table `apartamento` */

DROP TABLE IF EXISTS `apartamento`;

CREATE TABLE `apartamento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `andar` int(11) DEFAULT NULL,
  `numero` varchar(255) DEFAULT NULL,
  `idBloco` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC26F6FC4380DD0AB` (`idBloco`),
  KEY `FK_gfguw16uodchnv5amnt2ptf6j` (`idBloco`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Table structure for table `areacomum` */

DROP TABLE IF EXISTS `areacomum`;

CREATE TABLE `areacomum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dataAbertura` datetime DEFAULT NULL,
  `dataFechamento` datetime DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `instrucoesReserva` varchar(255) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `podeSerReservado` bit(1) DEFAULT NULL,
  `taxaReserva` double DEFAULT NULL,
  `idCondominio` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6FE5E32CE47B492D` (`idCondominio`),
  KEY `FK_2wxrxn62db349i971oj02y13u` (`idCondominio`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

/*Table structure for table `arquivoataassembleia` */

DROP TABLE IF EXISTS `arquivoataassembleia`;

CREATE TABLE `arquivoataassembleia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caminho` varchar(500) DEFAULT NULL,
  `extensao` varchar(5) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `nomeOriginal` varchar(500) DEFAULT NULL,
  `tamanho` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Table structure for table `arquivodocumento` */

DROP TABLE IF EXISTS `arquivodocumento`;

CREATE TABLE `arquivodocumento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caminho` varchar(500) DEFAULT NULL,
  `extensao` varchar(5) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `nomeOriginal` varchar(500) DEFAULT NULL,
  `tamanho` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Table structure for table `arquivoindicacao` */

DROP TABLE IF EXISTS `arquivoindicacao`;

CREATE TABLE `arquivoindicacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caminho` varchar(500) DEFAULT NULL,
  `destaque` tinyint(1) DEFAULT NULL,
  `extensao` varchar(5) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `nomeOriginal` varchar(500) DEFAULT NULL,
  `tamanho` bigint(20) DEFAULT NULL,
  `idIndicacao` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2k3va0j1mx93gguu9fd0i67xo` (`idIndicacao`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `arquivonoticia` */

DROP TABLE IF EXISTS `arquivonoticia`;

CREATE TABLE `arquivonoticia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caminho` varchar(500) DEFAULT NULL,
  `extensao` varchar(5) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `nomeOriginal` varchar(500) DEFAULT NULL,
  `tamanho` int(11) DEFAULT NULL,
  `idNoticia` bigint(20) NOT NULL,
  `destaque` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s2w48peifh5h6hpg0lb4cmmdo` (`idNoticia`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Table structure for table `arquivousuario` */

DROP TABLE IF EXISTS `arquivousuario`;

CREATE TABLE `arquivousuario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caminho` varchar(500) DEFAULT NULL,
  `extensao` varchar(5) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `nomeOriginal` varchar(500) DEFAULT NULL,
  `tamanho` bigint(20) DEFAULT NULL,
  `altura` int(11) DEFAULT NULL,
  `largura` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Table structure for table `assembleia` */

DROP TABLE IF EXISTS `assembleia`;

CREATE TABLE `assembleia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ata` longtext,
  `conteudo` varchar(10000) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `dataCadastro` datetime DEFAULT NULL,
  `horario1` time DEFAULT NULL,
  `horario2` time DEFAULT NULL,
  `idAta` bigint(20) DEFAULT NULL,
  `idCondominio` bigint(20) DEFAULT NULL,
  `permitirPautas` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9g4gvec6pieqtp7ee4e2ftral` (`idAta`),
  KEY `FK_nd38twkpa40aqjm9nu8lvkw2g` (`idCondominio`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Table structure for table `avaliacao` */

DROP TABLE IF EXISTS `avaliacao`;

CREATE TABLE `avaliacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `observacao` varchar(500) DEFAULT NULL,
  `positiva` tinyint(1) DEFAULT NULL,
  `idUsuario` bigint(20) NOT NULL,
  `idBatePapo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK50A7F0795E946FA5` (`idUsuario`),
  KEY `FK_smraavx87dcaqaus9e33q79p2` (`idUsuario`),
  KEY `FK_e38l7abi1a1ogvej6342ows2f` (`idBatePapo`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

/*Table structure for table `avaliacaobatepapo` */

DROP TABLE IF EXISTS `avaliacaobatepapo`;

CREATE TABLE `avaliacaobatepapo` (
  `idBatePapo` bigint(20) NOT NULL,
  `idAvaliacao` bigint(20) NOT NULL,
  PRIMARY KEY (`idBatePapo`,`idAvaliacao`),
  KEY `FK_kvwixri6o83qpkfsm3bfiouvi` (`idBatePapo`),
  KEY `FK9501B2599522F937` (`idBatePapo`),
  KEY `FK9501B2593058FC3B` (`idAvaliacao`),
  KEY `FK_t21yxmnyotl86ndyf9wng20ag` (`idAvaliacao`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `avaliacaoindicacao` */

DROP TABLE IF EXISTS `avaliacaoindicacao`;

CREATE TABLE `avaliacaoindicacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avaliacao` int(11) DEFAULT NULL,
  `comentario` varchar(1000) DEFAULT NULL,
  `idIndicacao` bigint(20) DEFAULT NULL,
  `idUsuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_11acrn9js788e0pe8sc38lbsb` (`idIndicacao`),
  KEY `FK_7tn8ihym0hka2dnsusj2ugvxe` (`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `batepapo` */

DROP TABLE IF EXISTS `batepapo`;

CREATE TABLE `batepapo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mensagem` varchar(800) DEFAULT NULL,
  `idCondominio` bigint(20) NOT NULL,
  `idUsuario` bigint(20) NOT NULL,
  `data` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9EF14AE05E946FA5` (`idUsuario`),
  KEY `FK9EF14AE0E47B492D` (`idCondominio`),
  KEY `FK_isdtch1m6hsuxhafyl81tqoap` (`idCondominio`),
  KEY `FK_fgl05j115y7p3x7vtyk82lc9d` (`idUsuario`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Table structure for table `bloco` */

DROP TABLE IF EXISTS `bloco`;

CREATE TABLE `bloco` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `numero` int(11) DEFAULT NULL,
  `qtdAndares` int(11) DEFAULT NULL,
  `idCondominio` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3D4D471E47B492D` (`idCondominio`),
  KEY `FK_fajboh6dpfwfg6kn9kocf1uxx` (`idCondominio`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Table structure for table `categoriaservicoproduto` */

DROP TABLE IF EXISTS `categoriaservicoproduto`;

CREATE TABLE `categoriaservicoproduto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(10000) DEFAULT NULL,
  `nome` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `cidade` */

DROP TABLE IF EXISTS `cidade`;

CREATE TABLE `cidade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `uf` varchar(4) NOT NULL,
  `estado` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pxn0exjdh72lvrwlymvmwp2r4` (`estado`)
) ENGINE=MyISAM AUTO_INCREMENT=9715 DEFAULT CHARSET=latin1;

/*Table structure for table `comentariobatepapo` */

DROP TABLE IF EXISTS `comentariobatepapo`;

CREATE TABLE `comentariobatepapo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comentario` varchar(1000) DEFAULT NULL,
  `idBatePapo` bigint(20) DEFAULT NULL,
  `idUsuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ngdhpni5ell5da0n1p6t5g9bs` (`idBatePapo`),
  KEY `FK_qqt4icwlbihy1dn5tpmoq7rj6` (`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `condominio` */

DROP TABLE IF EXISTS `condominio`;

CREATE TABLE `condominio` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `qtdAptos` int(11) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `cep` varchar(20) DEFAULT NULL,
  `complemento` varchar(100) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `idCidade` bigint(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qp2qyxowxakc8rhdayp1a7074` (`idCidade`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

/*Table structure for table `documento` */

DROP TABLE IF EXISTS `documento`;

CREATE TABLE `documento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ativo` tinyint(1) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `descricao` mediumtext,
  `titulo` varchar(400) DEFAULT NULL,
  `idArquivo` bigint(20) DEFAULT NULL,
  `idCondominio` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4blcy66jkixml1m2h5kb7751l` (`idArquivo`),
  KEY `FK_fec3n3ab3nw8cvsen5i8xcgqa` (`idCondominio`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Table structure for table `estado` */

DROP TABLE IF EXISTS `estado`;

CREATE TABLE `estado` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(20) NOT NULL,
  `uf` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

/*Table structure for table `indicacao` */

DROP TABLE IF EXISTS `indicacao`;

CREATE TABLE `indicacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comentario` varchar(10000) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `destaque` tinyint(1) DEFAULT NULL,
  `idCategoria` bigint(20) DEFAULT NULL,
  `idCondominio` bigint(20) DEFAULT NULL,
  `idUsuario` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_38h4wnm0s569k4vrd5vh5xfut` (`idCategoria`),
  KEY `FK_eeyvb2ssr50rungs6ixkykitx` (`idCondominio`),
  KEY `FK_er6l3wvolg5aoa4mgis6ylr2c` (`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `mensagemsindico` */

DROP TABLE IF EXISTS `mensagemsindico`;

CREATE TABLE `mensagemsindico` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `mensagem` longtext,
  `idCondominio` bigint(20) DEFAULT NULL,
  `idUusario` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_o7a2mcf1x2krbbjbd78rf455x` (`idCondominio`),
  KEY `FK_bgajiukgj9msu86sk0m3fk6i2` (`idUusario`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Table structure for table `noticia` */

DROP TABLE IF EXISTS `noticia`;

CREATE TABLE `noticia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ativa` tinyint(1) DEFAULT NULL,
  `conteudo` mediumtext,
  `data` date DEFAULT NULL,
  `destaque` tinyint(1) DEFAULT NULL,
  `titulo` varchar(400) DEFAULT NULL,
  `idCondominio` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_k2ojpgshf6petynpx5ntfk4ro` (`idCondominio`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Table structure for table `opcaovotacao` */

DROP TABLE IF EXISTS `opcaovotacao`;

CREATE TABLE `opcaovotacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(2000) NOT NULL,
  `idVotacao` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n963wyycbpe2yalykbxpl8esf` (`idVotacao`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Table structure for table `parametrosistema` */

DROP TABLE IF EXISTS `parametrosistema`;

CREATE TABLE `parametrosistema` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `valor` varchar(100) NOT NULL,
  `idCondominio` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK25BFCEC9E47B492D` (`idCondominio`),
  KEY `FK_jbofvb5nh1u5098j53i4yn7rv` (`idCondominio`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Table structure for table `pautaassembleia` */

DROP TABLE IF EXISTS `pautaassembleia`;

CREATE TABLE `pautaassembleia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aprovada` tinyint(1) DEFAULT NULL,
  `mensagem` varchar(1000) DEFAULT NULL,
  `idAssembleia` bigint(20) NOT NULL,
  `idUsuario` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4q8yvgo0tvem2u7lv4q71xx7b` (`idAssembleia`),
  KEY `FK_kv42v0kvvjn09gsa8xmvgueyo` (`idUsuario`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Table structure for table `reserva` */

DROP TABLE IF EXISTS `reserva`;

CREATE TABLE `reserva` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` datetime NOT NULL,
  `horaFim` datetime DEFAULT NULL,
  `horaInicio` datetime DEFAULT NULL,
  `idAreaComum` bigint(20) DEFAULT NULL,
  `idUsuario` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA49CA4985E946FA5` (`idUsuario`),
  KEY `FKA49CA4987097D1C1` (`idAreaComum`),
  KEY `FK_q3tick0lekdcmwy4juidearqg` (`idAreaComum`),
  KEY `FK_8rwy54mihywafdec8jpnexwcx` (`idUsuario`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATIVO` tinyint(1) DEFAULT NULL,
  `DATA` datetime DEFAULT NULL,
  `DATA_EXPIRACAO` datetime DEFAULT NULL,
  `TOKEN` varchar(255) DEFAULT NULL,
  `CHAVE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Table structure for table `usuario` */

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `idCondominio` bigint(20) DEFAULT NULL,
  `fotoPerfil` varchar(300) DEFAULT NULL,
  `tipoUsuario` varchar(30) NOT NULL,
  `idApartamento` bigint(20) DEFAULT NULL,
  `celular` varchar(20) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `idImagem` bigint(20) DEFAULT NULL,
  `cadastroConfirmado` tinyint(1) DEFAULT NULL,
  `dataCadastro` datetime DEFAULT NULL,
  `senha` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5B4D8B0EE47B492D` (`idCondominio`),
  KEY `FK_4n8iunbjvd9etv9uoj1awkqi6` (`idCondominio`),
  KEY `FK_exy5a4d5tb5ds8ff321f1sogr` (`idApartamento`),
  KEY `FK_l2gr8fd2v5xrd2r4ujscf6ymt` (`idImagem`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

/*Table structure for table `votacao` */

DROP TABLE IF EXISTS `votacao`;

CREATE TABLE `votacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assuntoVotacao` varchar(2000) NOT NULL,
  `dataLimite` datetime DEFAULT NULL,
  `tipoVotacao` varchar(20) NOT NULL,
  `idCondominio` bigint(20) NOT NULL,
  `idUsuario` bigint(20) NOT NULL,
  `ativa` tinyint(1) DEFAULT NULL,
  `votacaoOficial` tinyint(1) DEFAULT NULL,
  `resultadoParcial` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK8952130B5E946FA5` (`idUsuario`),
  KEY `FK8952130BE47B492D` (`idCondominio`),
  KEY `FK_3llpp6bcaokngaxuc7gvgbmgc` (`idCondominio`),
  KEY `FK_gegir99tsaqdc8ntmfh5a5ouq` (`idUsuario`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Table structure for table `voto` */

DROP TABLE IF EXISTS `voto`;

CREATE TABLE `voto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bit(1) DEFAULT NULL,
  `dataFim` bit(1) DEFAULT NULL,
  `dataInicio` bit(1) DEFAULT NULL,
  `sim` bit(1) DEFAULT NULL,
  `total` bit(1) DEFAULT NULL,
  `valor` bit(1) DEFAULT NULL,
  `opcao` int(11) DEFAULT NULL,
  `idVotacao` bigint(20) DEFAULT NULL,
  `dataVotacao` datetime DEFAULT NULL,
  `moeda` double DEFAULT NULL,
  `numero` double DEFAULT NULL,
  `idOpcao` bigint(20) DEFAULT NULL,
  `idUsuario` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8or2hn0xdy7i7bkirh4qjl4ub` (`idVotacao`),
  KEY `FK_nvnydthmfg7olweeno3ga5ndw` (`idOpcao`),
  KEY `FK_6dq4v95xntiygr1telpght52j` (`idUsuario`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
