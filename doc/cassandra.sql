create keyspace evgeny_photomark with replication = {
  'class' : 'NetworkTopologyStrategy',
  'dc' : 1
  };

use evgeny_photomark;

create table photometa
(
  code text primary key,
  clientid int,
  ext text,
  linename text
);