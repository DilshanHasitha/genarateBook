import { IAvatarAttributes } from 'app/entities/avatar-attributes/avatar-attributes.model';
import { ILayerGroup } from 'app/entities/layer-group/layer-group.model';
import { ICharacter } from 'app/entities/character/character.model';

export interface IAvatarCharactor {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  imgUrl?: string | null;
  width?: number | null;
  height?: number | null;
  x?: number | null;
  y?: number | null;
  avatarAttributes?: Pick<IAvatarAttributes, 'id'>[] | null;
  layerGroup?: Pick<ILayerGroup, 'id'> | null;
  character?: Pick<ICharacter, 'id' | 'code'> | null;
}

export type NewAvatarCharactor = Omit<IAvatarCharactor, 'id'> & { id: null };
